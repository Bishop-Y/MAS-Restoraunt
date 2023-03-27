package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.cafe.Main;
import java.util.logging.Logger;
import org.cafe.model.Equipment;
import org.cafe.model.Operation;
import org.cafe.model.OperationWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProcessAgent extends Agent {
    private final ConcurrentHashMap<AID, Boolean> cookTable = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Equipment, Boolean> equipmentTable = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<OperationWrapper> jobs = new ConcurrentLinkedQueue<>();
    public static final String AGENT_TYPE = "Process";

    private int lastBundle = 0;
    private int currentBundle = 0;

    private void register(String type, String name) {
        Logger log = Logger.getLogger(getClass().getName());
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(this.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            log.info("Registered " + this.getName());
        } catch (FIPAException fe) {
            fe.printStackTrace();
            log.warning("Failed to register " + this.getName());
        }
    }

    @Override
    protected void setup() {
        super.setup();
        register(AGENT_TYPE, "process-agent");
        fillTable(cookTable, CookAgent.AGENT_TYPE);
        var args = getArguments();
        List<Equipment> equipment = (List<Equipment>) Arrays.stream(args).findFirst().get();
        equipment.forEach(x -> equipmentTable.put(x, true));
        addBehaviour(new PushOperationsBehaviour());
        addBehaviour(new PopJobBehaviour());
    }

    private void fillTable(ConcurrentHashMap<AID, Boolean> table, String agentType) {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();

        serviceDescription.setType(agentType);
        template.addServices(serviceDescription);
        try {
            DFAgentDescription[] result = DFService.search(this, template);

            for (DFAgentDescription agentDescription : result) {
                table.put(agentDescription.getName(), true);
            }
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }

    private class PushOperationsBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("operations");
            ACLMessage message = receive(mt);
            if (message != null) {
                Main.append("PushOperationsBehaviour");
                List<Operation> operations;
                try {
                    operations = (List<Operation>) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                ++lastBundle;
                jobs.addAll(operations.stream().map(x -> new OperationWrapper(lastBundle, x)).toList());
            }
        }
    }

    private class PopJobBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            if (!jobs.isEmpty()) {
                AID cook = null;
                for (var key : cookTable.keySet()) {
                    if (Boolean.TRUE.equals(cookTable.get(key))) {
                        cook = key;
                    }
                }
                Equipment equipment = null;
                for (var key : equipmentTable.keySet()) {
                    if (Boolean.TRUE.equals(equipmentTable.get(key))) {
                        if (key.getTypeId() == jobs.peek().getOperation().getEquipmentType()) {
                            equipment = key;
                        }
                    }
                }
                if (cook != null && equipment != null) {
                    Main.append("PopJobBehaviour");
                    equipmentTable.put(equipment, false);
                    cookTable.put(cook, false);
                    ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
                    push.setOntology("get-job");
                    push.addReceiver(cook);
                    try {
                        push.setContentObject(jobs.peek().getOperation().getTime());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    jobs.poll();
                    send(push);
                    addBehaviour(new JobDoneBehaviour(cook, equipment));
                }
            } else {
                block();
            }
        }

        private class JobDoneBehaviour extends Behaviour {
            private boolean isDone = false;
            private final AID cook;
            private final Equipment equipment;

            public JobDoneBehaviour(AID cook, Equipment equipment) {
                this.cook = cook;
                this.equipment = equipment;
            }

            @Override
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                        .MatchOntology("job-done");
                ACLMessage message = receive(mt);
                if (message != null) {
                    Main.append("JobDoneBehaviour");
                    cookTable.put(cook, true);
                    equipmentTable.put(equipment, true);
                    isDone = true;
                } else {
                    block();
                }
            }

            @Override
            public boolean done() {
                return isDone;
            }
        }
    }
}

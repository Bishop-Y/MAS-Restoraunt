package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.cafe.Main;
import org.cafe.behaviour.RegisterBehaviour;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CookAgent extends Agent {

    public static final String AGENT_TYPE = "Cook";

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
        register(AGENT_TYPE, "cook-agent");
        addBehaviour(new GetJobBehaviour());
    }

    private class GetJobBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("get-job");
            ACLMessage message = receive(mt);
            if (message != null) {
                Main.append("GetJobBehaviour");
                double time = -1;
                try {
                    time = (double)message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (time != -1) {
                    time *= 100;
                    try {
                        TimeUnit.MILLISECONDS.sleep((long) time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
                push.setOntology("job-done");
                push.addReceiver(new AID("process-agent", AID.ISLOCALNAME));
                send(push);
            } else {
                block();
            }
        }
    }
}

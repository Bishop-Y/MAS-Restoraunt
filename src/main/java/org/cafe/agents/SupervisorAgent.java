package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.cafe.Main;
import org.cafe.behaviour.RegisterBehaviour;
import org.cafe.model.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupervisorAgent extends Agent {
    public static final String AGENT_TYPE = "Supervisor";


    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterBehaviour(AGENT_TYPE, "supervisor-agent"));
        addBehaviour(new GetOrderBehaviour());
    }


    private class GetOrderBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("prepare-order");
            ACLMessage message = receive(mt);
            if (message != null) {
                Main.append("GetOrderBehaviour");
                VisitorOrder order;
                try {
                    order = (VisitorOrder) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (order != null) {
                    addBehaviour(new GetOperationsBehaviour(order));
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    send(reply);
                } else {
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    send(reply);
                }
            } else {
                block();
            }
        }
    }

    private class GetOperationsBehaviour extends Behaviour {
        private final VisitorOrder order;

        public GetOperationsBehaviour(VisitorOrder order) {
            this.order = order;
        }

        private boolean isDone = false;

        @Override
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setOntology("get-operations");
            message.addReceiver(new AID("menu-agent", AID.ISLOCALNAME));
            try {
                message.setContentObject(order);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(message);

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    .MatchOntology("send-operations");
            ACLMessage received = receive(mt);
            if (received != null) {
                Main.append("GetOperationsBehaviour");
                List<Operation> operations;
                try {
                    operations = (List<Operation>) received.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (operations != null) {
                    addBehaviour(new TakeProductsBehaviour(operations));
                    ACLMessage reply = received.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    send(reply);
                } else {
                    ACLMessage reply = received.createReply();
                    reply.setPerformative(ACLMessage.FAILURE);
                    send(reply);
                }
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

    private class TakeProductsBehaviour extends Behaviour {
        private List<Operation> operations = new ArrayList<>();
        private boolean isDone = false;

        public TakeProductsBehaviour(List<Operation> operations) {

            this.operations = operations;
        }

        @Override
        public void action() {
            Main.append("TakeProductsBehaviour");
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setOntology("take-products");
            message.addReceiver(new AID("storage-agent", AID.ISLOCALNAME));
            List<OperationProduct> operationProducts = new ArrayList<>();
            for (Operation operation : operations) {
                operationProducts.addAll(operation.getProductsList());
            }
            try {
                message.setContentObject((Serializable) operationProducts);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(message);
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("products-taken");
            ACLMessage received = receive(mt);
            if (received != null) {
                addBehaviour(new SendOperationsToProcessBehaviour(operations));
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

    private class SendOperationsToProcessBehaviour extends OneShotBehaviour {
        private List<Operation> operations = new ArrayList<>();

        public SendOperationsToProcessBehaviour(List<Operation> operations) {

            this.operations = operations;
        }

        @Override
        public void action() {
            Main.append("Send data");
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setOntology("operations");
            message.addReceiver(new AID("process-agent", AID.ISLOCALNAME));
            try {
                message.setContentObject((Serializable) operations);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(message);
        }
    }
}

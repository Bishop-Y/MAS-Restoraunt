package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.cafe.Main;
import org.cafe.behaviour.RegisterBehaviour;
import org.cafe.model.VisitorOrder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class VisitorAgent extends Agent {
    private VisitorOrder visitorOrder;
    public static final String AGENT_TYPE = "Visitor";


    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterBehaviour(AGENT_TYPE, "visitor-agent"));
        addBehaviour(new SendWishedDishBehaviour());
        var args = getArguments();
        visitorOrder = (VisitorOrder) Arrays.stream(args).findFirst().get();
    }

    private class SendWishedDishBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setOntology("get-order");
            message.addReceiver(new AID("waiter-agent", AID.ISLOCALNAME));
            try {
                message.setContentObject((Serializable) visitorOrder);
                Main.append("Visitor: " + visitorOrder);
                Main.append("SendWishedDishBehaviour");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(message);

            addBehaviour(new ReceiptBehaviour());

        }
    }

    private class ReceiptBehaviour extends Behaviour {
        private boolean gotReceipt = false;

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("receipt");
            ACLMessage reply = receive(mt);
            if (reply != null) {
                VisitorOrder receipt;
                try {
                    receipt = (VisitorOrder) reply.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                gotReceipt = true;
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return gotReceipt;
        }
    }
}

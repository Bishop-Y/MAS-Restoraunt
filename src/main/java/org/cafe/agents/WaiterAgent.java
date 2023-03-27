package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import org.apache.commons.lang3.time.StopWatch;
import org.cafe.Main;
import org.cafe.behaviour.RegisterBehaviour;
import org.cafe.model.MenuDish;
import org.cafe.model.VisitorOrder;

import java.io.IOException;
import java.util.List;

public class WaiterAgent extends Agent {
    public static final String AGENT_TYPE = "Waiter";

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterBehaviour(AGENT_TYPE, "waiter-agent"));
        addBehaviour(new ValidateOrderBehaviour());
    }

    private class ValidateOrderBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            // Получили заказ
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("get-order");
            ACLMessage message = receive(mt);
            if (message != null) {
                Main.append("ValidateOrderBehaviour");
                VisitorOrder order;
                try {
                    order = (VisitorOrder) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                // Получили меню
                ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
                push.setOntology("get-menu");
                push.addReceiver(new AID("menu-agent", AID.ISLOCALNAME));
                send(push);
                addBehaviour(new ReceivedMenuBehaviour(order));

            } else {
                block();
            }
        }
    }

    private class ReceivedMenuBehaviour extends Behaviour {
        private final VisitorOrder order;

        private boolean isDone = false;

        private ReceivedMenuBehaviour(VisitorOrder order) {
            this.order = order;
        }

        @Override
        public void action() {
            MessageTemplate mtR = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("send-menu");
            ACLMessage received = receive(mtR);
            if (received != null) {
                List<MenuDish> menu;
                try {
                    menu = (List<MenuDish>) received.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                boolean checkOrder = true;
                for (var orderDish : order.getOrderedDishes()) {
                    MenuDish found = null;
                    for (var menuDish : menu) {
                        if (menuDish.getId() == orderDish.getDish() && menuDish.isActive()) {
                            found = menuDish;
                        }
                    }
                    if (found == null) {
                        checkOrder = false;
                        break;
                    }
                }
                if (checkOrder) {
                    addBehaviour(new SendOrderBehaviour(order));
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

    private class SendOrderBehaviour extends OneShotBehaviour {

        private final VisitorOrder visitorOrder;

        private SendOrderBehaviour(VisitorOrder visitorOrder) {
            this.visitorOrder = visitorOrder;
        }

        @Override
        public void action() {
            StopWatch watch = new StopWatch();
            Main.append("SendOrderBehaviour");
            watch.start();
            ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
            push.setOntology("prepare-order");
            push.addReceiver(new AID("supervisor-agent", AID.ISLOCALNAME));
            try {
                push.setContentObject(visitorOrder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(push);
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("done-order");
            ACLMessage message = receive(mt);
            if (message != null) {
                VisitorOrder receipt;
                try {
                    receipt = (VisitorOrder) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                ACLMessage pushToVisitor = new ACLMessage(ACLMessage.REQUEST);
                pushToVisitor.setOntology("receipt");
                pushToVisitor.addReceiver(new AID("visitor-agent", AID.ISLOCALNAME));
                try {
                    pushToVisitor.setContentObject(receipt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                watch.stop();
                long time = watch.getTime();
                time /= 10;
                Main.append("VISITOR ORDER TIME: " + time);
                visitorOrder.setOrderEnded(visitorOrder.getOrderStarted().plusSeconds(time));
                send(pushToVisitor);
            }
        }
    }
}

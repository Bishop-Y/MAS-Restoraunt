package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MenuAgent extends Agent {

    private List<MenuDish> menu = new ArrayList<>();
    private List<DishCard> dishCards = new ArrayList<>();
    public static final String AGENT_TYPE = "Menu";


    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterBehaviour(AGENT_TYPE, "menu-agent"));
        var args = getArguments();
        menu = (List<MenuDish>) Arrays.stream(args).findFirst().get();
        dishCards = (List<DishCard>) Arrays.stream(args).skip(1).findFirst().get();
        addBehaviour(new UpdateMenuBehaviour());
        addBehaviour(new GetMenuBehaviour());
        addBehaviour(new OperationsBehaviour());
    }

    private class UpdateMenuBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setOntology("get-products");
            message.addReceiver(new AID("storage-agent", AID.ISLOCALNAME));
            send(message);

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM)
                    .MatchOntology("products-list");
            ACLMessage reply = receive(mt);
            if (reply != null) {
                List<Product> products;
                try {
                    products = (List<Product>) reply.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                var availableProducts = new ArrayList<>(products);
                for (var dish : menu) {
                    DishCard dishCard = null;
                    for (var card : dishCards) {
                        if (card.getId() == dish.getCard()) {
                            dishCard = card;
                        }
                    }
                    HashMap<Integer, List<OperationProduct>> map = new HashMap<>();
                    if (dishCard != null) {
                        for (var operation : dishCard.getOperationsList()) {
                            for (var product : operation.getProductsList()) {
                                if (map.containsKey(product.getType())) {
                                    map.get(product.getType()).add(product);
                                } else {
                                    map.put(product.getType(), new ArrayList<>());
                                    map.get(product.getType()).add(product);
                                }
                            }
                        }
                        HashMap<Integer, Double> help_map = new HashMap<>();
                        for (var key : map.keySet()) {
                            double sum = 0.0;
                            for (var operProduct : map.get(key)) {
                                sum += operProduct.getQuantity();
                            }
                            help_map.put(key, sum);
                        }
                        for (Product product : availableProducts) {
                            var tmp = help_map.get(product.getType());
                            if (tmp != null && tmp > product.getQuantity()) {
                                dish.setActive(false);
                            }
                        }
                    }
                }
            } else {
                block();
            }
        }
    }

    private class GetMenuBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    .MatchOntology("get-menu");
            ACLMessage message = receive(mt);
            if (message != null) {
                Main.append("GetMenuBehaviour");
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setOntology("send-menu");

                try {
                    reply.setContentObject((Serializable) menu);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                send(reply);
            } else {
                block();
            }
        }
    }

    private class OperationsBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    .MatchOntology("get-operations");
            ACLMessage message = receive(mt);
            if (message != null) {
                VisitorOrder order;
                try {
                    order = (VisitorOrder) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (order != null) {
                    List<Operation> operations = new ArrayList<>();
                    for (var orderDish : order.getOrderedDishes()) {
                        DishCard dishCard = null;
                        MenuDish found = null;
                        for (var dish : menu) {
                            if (dish.getId() == orderDish.getDish()) {
                                found = dish;
                            }
                        }
                        if (found != null) {
                            for (var card : dishCards) {
                                if (card.getId() == found.getCard()) {
                                    dishCard = card;
                                }
                            }
                        }
                        if (dishCard != null) {
                            operations.addAll(dishCard.getOperationsList());
                            addBehaviour(new SendOperationsBehaviour(operations));
                        }
                    }
                } else {
                    block();
                }
            } else {
                block();
            }
        }
    }

    private class SendOperationsBehaviour extends OneShotBehaviour {

        private List<Operation> operations = new ArrayList<>();

        private SendOperationsBehaviour(List<Operation> operations) {
            this.operations = operations;
        }

        @Override
        public void action() {
            ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
            push.setOntology("send-operations");
            push.addReceiver(new AID("supervisor-agent", AID.ISLOCALNAME));
            try {

                push.setContentObject((Serializable) operations);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            send(push);
        }
    }
}
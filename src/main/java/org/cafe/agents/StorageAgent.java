package org.cafe.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.UnreadableException;
import org.cafe.Main;
import org.cafe.behaviour.RegisterBehaviour;
import org.cafe.model.OperationProduct;
import org.cafe.model.Product;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StorageAgent extends Agent {

    private List<Product> availableProducts = new ArrayList<>();
    public static final String AGENT_TYPE = "Storage";

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new RegisterBehaviour(AGENT_TYPE, "storage-agent"));
        addBehaviour(new GetProductsBehaviour());
        addBehaviour(new ProductBehaviour());
        var args = getArguments();
        availableProducts = (List<Product>) Arrays.stream(args).findFirst().get();
    }

    private class GetProductsBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    .MatchOntology("get-products");
            ACLMessage message = receive(mt);
            if (message != null) {
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setOntology("products-list");
                try {
                    reply.setContentObject((Serializable) availableProducts);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                send(reply);
            } else {
                block();
            }
        }
    }

    private class ProductBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
                    .MatchOntology("take-products");
            ACLMessage message = receive(mt);
            if (message != null) {
                List<OperationProduct> products;
                try {
                    products = (List<OperationProduct>) message.getContentObject();
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
                if (products != null) {
                    for (var product : products) {
                        for (int i = 0; i < availableProducts.size(); ++i) {
                            if (availableProducts.get(i).getType() == product.getType()) {
                                double quantity = availableProducts.get(i).getQuantity();
                                availableProducts.get(i).setQuantity(quantity - product.getQuantity());
                                break;
                            }
                        }
                    }
                    ACLMessage push = new ACLMessage(ACLMessage.REQUEST);
                    push.setOntology("products-taken");
                    push.addReceiver(new AID("supervisor-agent", AID.ISLOCALNAME));
                    Main.append("ProductBehaviour");
                    send(push);
                } else {
                    ACLMessage push = new ACLMessage(ACLMessage.FAILURE);
                    push.setOntology("products-taken");
                    push.addReceiver(new AID("supervisor-agent", AID.ISLOCALNAME));
                    send(push);
                }
            } else {
                block();
            }
        }
    }
}

package org.cafe.behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.logging.Logger;

public class RegisterBehaviour extends OneShotBehaviour {

    private final String type;
    private final String name;
    private final Logger log = Logger.getLogger(getClass().getName());

    public RegisterBehaviour(String type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    @Override
    public void action() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(myAgent.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);
        dfd.addServices(sd);
        try {
            DFService.register(myAgent, dfd);
            log.info("Registered " + myAgent.getName());
        } catch (FIPAException fe) {
            fe.printStackTrace();
            log.warning("Failed to register " + myAgent.getName());
        }
    }
}

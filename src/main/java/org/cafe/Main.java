package org.cafe;

import jade.core.Agent;
import jade.wrapper.StaleProxyException;
import org.cafe.agents.*;
import org.cafe.model.lists.*;
import org.cafe.util.JsonParser;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main extends Agent {

    public static synchronized void append(String text) {
        System.out.println(text);
    }

    public static void main(String[] args) throws IOException, StaleProxyException, InterruptedException {
        MainController mainController = new MainController();
        JsonParser jsonParser = new JsonParser();
        var products = jsonParser.parse("storage.txt", ProductList.class);
        var menu = jsonParser.parse("menu.txt", MenuDishList.class);
        var dishCards = jsonParser.parse("dish_cards.txt", DishCardList.class);
        var visitors = jsonParser.parse("visitors.txt", VisitorOrderList.class);
        var cookers = jsonParser.parse("cookers.txt", CookList.class);
        var equipment = jsonParser.parse("equipment.txt", EquipmentList.class);
        var storageAgent = mainController.createAgent(StorageAgent.class, "storage-agent", products.getProducts());
        var menuAgent = mainController.createAgent(MenuAgent.class, "menu-agent", menu.getMenuDishes(), dishCards.getDishCards());
        var waiter = mainController.createAgent(WaiterAgent.class, "waiter-agent");
        var supervisor = mainController.createAgent(SupervisorAgent.class, "supervisor-agent");
        storageAgent.start();
        menuAgent.start();
        waiter.start();
        supervisor.start();
        int i = 0;
        mainController.createAgent(ProcessAgent.class, "process-agent", equipment.getEquipment()).start();
        for (var ignored : cookers.getCookers()) {
            mainController.createAgent(CookAgent.class, "cook-agent" + i).start();
            ++i;
        }
        i = 0;
        for (var visitor : visitors.getVisitorOrders()) {
            TimeUnit.SECONDS.sleep(1);
            mainController.createAgent(VisitorAgent.class, "visitor-agent" + i, visitor).start();
            ++i;
        }
    }
}
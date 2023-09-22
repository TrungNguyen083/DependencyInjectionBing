package org.example;

import org.example.controller.AdArticleController;
import org.example.restapi.ControllerManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(Main.class.getName());
        int PORT = 8080;

        ControllerManager customHttpServer = new ControllerManager(PORT);
        customHttpServer.addController(AdArticleController.class);
        customHttpServer.start();

        logger.log(Level.FINE, "Server is listening on port {}" ,PORT);
    }
}
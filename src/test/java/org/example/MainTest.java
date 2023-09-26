package org.example;

import org.example.controller.AdController;
import org.example.dilibrary.MyContainer;
import org.example.controller.AdArticleController;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MainTest {

    @Test
    public void testGetClass() throws ClassNotFoundException {
        var clazz = Class.forName("org.example.controller.AdArticleController");
        assertNotNull(clazz);
    }

    @Test
    public void testScanAllController() throws Exception {
        MyContainer container = new MyContainer();

        // Scan and register controller classes in a specific base package
        container.scanAndRegisterControllers();
        // Get instances of controller classes from the container
        AdArticleController adArticleController = container.getBean(AdArticleController.class);
        // Access controller methods
        String adArticleList = adArticleController.getAllAd();
        AdController adController = container.getBean(AdController.class);
        String allAd = adController.getAllAd();

        assertNotNull(adArticleList);
        assertNotNull(allAd);
    }

    @Test
    public void testAdController() throws Exception {
        MyContainer container = new MyContainer();

        container.registerBean(AdController.class);
        AdController adController = container.getBean(AdController.class);
        String adArticleList = adController.getAllAd();

        assertNotNull(adArticleList);
    }

    @Test
    public void testGetAllAdArticle() throws Exception {
        MyContainer container = new MyContainer();

        container.registerBean(AdArticleController.class);
        AdArticleController adArticleController = container.getBean(AdArticleController.class);
        String adArticleList = adArticleController.getAllAd();

        assertNotNull(adArticleList);
    }

    @Test
    public void testRegisterBean() throws Exception {
        MyContainer container = new MyContainer();

        container.registerBean(AdArticleController.class);

        assertNotNull(container);
    }

    @Test
    public void testGetBean() throws Exception {
        MyContainer container = new MyContainer();

        container.registerBean(AdArticleController.class);
        AdArticleController adArticleController = container.getBean(AdArticleController.class);

        assertNotNull(adArticleController);
    }

    @Test
    public void testGetAdByID() throws Exception {
        MyContainer container = new MyContainer();

        container.scanAndRegisterControllers();
        AdArticleController adArticleController = container.getBean(AdArticleController.class);

        String adArticle = adArticleController.getAdByID("0dcb1e40-bdba-41b2-8f31-da92cc65e74a");

        System.out.println(adArticle);
        assertNotNull(adArticle);
    }

    @Test
    public void testAddArticleWithRequestBody() throws Exception {
        MyContainer container = new MyContainer();

        container.scanAndRegisterControllers();
        AdArticleController adArticleController = container.getBean(AdArticleController.class);

        //requestBody with 5 fields: guid, adImage, adTitle, adSource, adLink
        String guid = UUID.randomUUID().toString();
        String requestBody = "{\"guid\":\"" + guid + "\",\"adImage\":\"https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png\",\"adTitle\":\"Google\",\"adSource\":\"https://www.google.com\",\"adLink\":\"https://www.google.com\"}";

        String adArticle = adArticleController.addAdArticle(requestBody);

        assertNotNull(adArticle);
    }
}
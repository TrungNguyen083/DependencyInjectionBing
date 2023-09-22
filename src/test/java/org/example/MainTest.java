package org.example;

import org.example.dilibrary.MyContainer;
import org.example.controller.AdArticleController;
import org.example.models.AdArticle;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

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
}
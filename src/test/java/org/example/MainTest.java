package org.example;


import org.example.DILibrary.MyContainer;
import org.example.DILibrary.MyInjector;
import org.example.controller.AdArticleController;
import org.example.models.AdArticle;
import org.example.repositories.CrudRepositoryCustom;
import org.example.repositories.NewsRepository;
import org.example.services.AdArticleService;
import org.example.services.NewsService;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class MainTest {
    @Test
    public void testScanAllController() throws Exception {
        MyContainer container = new MyContainer();

        // Scan and register controller classes in a specific base package
        container.scanAndRegisterControllers("E:\\BBV\\Source code\\testAutowired\\src\\main\\java\\org\\example\\controller");

        // Get instances of controller classes from the container
        AdArticleController adArticleController = container.getBean(AdArticleController.class);
        // Access controller methods
        List<AdArticle> adArticleList = adArticleController.getAllAd();
        adArticleList.forEach(AdArticle::printInfo);
    }

    @Test
    public void testGetAllAdArticle() throws Exception {
        MyContainer container = new MyContainer();
        container.registerBean(AdArticleController.class);
        AdArticleController adArticleController = container.getBean(AdArticleController.class);
        List<AdArticle> adArticleList = adArticleController.getAllAd();
        adArticleList.forEach(AdArticle::printInfo);

        assertNotNull(adArticleList);
    }

    @Test
    public void testRegisterAnyService() throws Exception {
        MyContainer container = new MyContainer();

        // Register and inject the UserService
        container.registerBean(UserService.class, NewsService.class);
        UserService userService = container.getBean(UserService.class);
        NewsService newsService = container.getBean(NewsService.class);

        userService.performAction();
        newsService.performAction();
    }
    @Test
    public void testServiceFieldAutowired() throws Exception {
        MyContainer container = new MyContainer();

        // Register and inject the UserService
        container.registerBean(UserService.class);
        UserService userService = container.getBean(UserService.class);

        userService.performAction();
    }

    @Test
    public void testServiceConstructorAutowired() throws Exception {
        MyContainer container = new MyContainer();

        // Register and inject the NewsService
        container.registerBean(NewsService.class);
        NewsService newsService = container.getBean(NewsService.class);

        newsService.performAction();
    }

    @Test
    public void testFieldAutowired() throws Exception {
        MyContainer container = new MyContainer();
        // Register instances with the container
        container.registerBean(CrudRepositoryCustom.class);
        container.registerBean(UserRepository.class);
        container.registerBean(UserService.class);

        // Inject dependencies using MyInjector
        MyInjector.injectDependencies(container.getBean(UserService.class), container);
        MyInjector.injectDependencies(container.getBean(UserRepository.class), container);

        // Call the performAction method
        container.getBean(UserService.class).performAction();
    }

    @Test
    public void testConstructorAutowired() throws Exception {
        MyContainer container = new MyContainer();
        // Register instances with the container
        container.registerBean(CrudRepositoryCustom.class);
        container.registerBean(NewsRepository.class);
        container.registerBean(NewsService.class);

        // Inject dependencies using MyInjector
        MyInjector.injectDependencies(container.getBean(NewsService.class), container);
        MyInjector.injectDependencies(container.getBean(NewsRepository.class), container);

        // Call the performAction method
        container.getBean(NewsService.class).performAction();
    }

    @Test
    public void testNotHaveAutowired() {
        UserService userService = new UserService();
        userService.performAction();
    }
}
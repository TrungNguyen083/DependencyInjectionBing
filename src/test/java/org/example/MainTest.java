package org.example;


import org.example.repositories.NewsRepository;
import org.example.services.NewsService;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.junit.Test;

public class MainTest {
    @Test
    public void testServiceAutowired() throws Exception {
        MyContainer container = new MyContainer();

        // Scan and register classes with @Service annotations in the "com.example.services" package
        container.scanAndRegister("org/example/services", container);

        // Now, you can use your managed beans
        UserService userService = container.getBean(UserService.class);
        userService.performAction();
    }

    @Test
    public void testFieldAutowired() throws Exception {
        MyContainer container = new MyContainer();
        // Register instances with the container
        container.registerBean(CrudRepository.class);
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
        container.registerBean(CrudRepository.class);
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
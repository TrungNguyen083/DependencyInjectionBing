package org.example;


import org.junit.Test;

public class MainTest {
    @Test
    public void testAutowired() throws Exception {
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
    public void testNotHaveAutowired() {
        UserService userService = new UserService();
        userService.performAction();
    }

}
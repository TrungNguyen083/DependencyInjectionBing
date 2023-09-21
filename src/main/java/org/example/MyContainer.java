package org.example;

import org.example.services.NewsService;
import org.example.services.UserService;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyContainer {
    private Map<Class<?>, Object> beanMap = new HashMap<>();

    public void scanAndRegister(String basePackage, MyContainer container) throws Exception {
//        List<Class<?>> classes = ClassScanner.findClassesWithAnnotation(basePackage, Service.class);
        List<Class<?>> classes = new ArrayList<>();
        classes.add(UserService.class);
        classes.add(NewsService.class);
        for (Class<?> clazz : classes) {
            registerBean(clazz);
            MyInjector.injectDependencies(beanMap.get(clazz), container);
        }
    }

    public void registerBean(Class<?> clazz) throws Exception {
        // Check if the class has a constructor annotated with @Autowired
        Constructor<?> constructor = findAutowiredConstructor(clazz);
        if (constructor == null) {
            // If there's no constructor with @Autowired, create an instance without parameters
            Object instance = clazz.getDeclaredConstructor().newInstance();
            beanMap.put(clazz, instance);
            return;
        }
        // Retrieve parameter types
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        // Create instances of constructor parameters and pass them to the constructor
        Object[] constructorParams = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> paramType = parameterTypes[i];
            constructorParams[i] = createInstance(paramType);
        }
        // Create an instance of the class using the constructor
        Object instance = constructor.newInstance(constructorParams);
        beanMap.put(clazz, instance);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(MyAutowired.class)) {
                return constructor;
            }
        }
        return null;
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        // Recursively create instances for constructor parameters
        if (beanMap.containsKey(clazz)) {
            return beanMap.get(clazz);
        } else {
            Constructor<?> constructor = findAutowiredConstructor(clazz);
            if (constructor != null) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] constructorParams = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    constructorParams[i] = createInstance(parameterTypes[i]);
                }
                Object instance = constructor.newInstance(constructorParams);
                beanMap.put(clazz, instance);
                return instance;
            } else {
                return clazz.getDeclaredConstructor().newInstance();
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beanMap.get(clazz));
    }
}

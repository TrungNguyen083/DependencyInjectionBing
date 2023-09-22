package org.example.DILibrary;

import org.example.DILibrary.annotation.Controller;
import org.example.DILibrary.annotation.MyAutowired;
import org.example.DILibrary.configuaration.ControllerConfig;
import org.example.configuration.ConfigService;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class MyContainer {
    private final Map<Class<?>, Object> beanMap;
    private final List<Class<?>> registeredClasses;
    ConfigService configService;

    private final ControllerConfig controllerConfig;

    public MyContainer() throws IOException {
        beanMap = new HashMap<>();
        registeredClasses = new ArrayList<>();
        configService = new ConfigService();
        controllerConfig = configService.readConfig("src\\main\\resources\\DIControllerConfig.json", ControllerConfig.class);
    }

    public void scanAndRegisterControllers() throws Exception {
        String basePackage = controllerConfig.getBasePackage();
        List<Class<?>> classes = ClassScanner.findClassesWithAnnotation(basePackage, Controller.class, controllerConfig);
        for (Class<?> clazz : classes) {
            registerBean(clazz);
        }
    }

    public void registerBean(Class<?> ... listClazz) throws Exception {
        for (Class<?> clazz : listClazz) {
            if (!registeredClasses.contains(clazz)) {
                registeredClasses.add(clazz);
                // Check if the class has a constructor annotated with @Autowired
                Constructor<?> constructor = findAutowiredConstructor(clazz);

                if (constructor != null) {
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    Object[] constructorParams = new Object[parameterTypes.length];

                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> paramType = parameterTypes[i];
                        Object dependency = getOrCreateBean(paramType);
                        constructorParams[i] = dependency;
                    }
                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance(constructorParams);
                    beanMap.put(clazz, instance);
                } else {
                    // If there's no constructor with @Autowired, create an instance without parameters
                    Object instance = createInstance(clazz);
                    beanMap.put(clazz, instance);
                }
                // Inject dependencies into fields
                injectFields(beanMap.get(clazz), clazz);
            }
        }
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(MyAutowired.class))
                .findFirst()
                .orElse(null);
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    private Object getOrCreateBean(Class<?> clazz) throws Exception {
        if (beanMap.containsKey(clazz)) {
            return beanMap.get(clazz);
        }
        // Register and return the bean
        registerBean(clazz);
        return beanMap.get(clazz);
    }

    private void injectFields(Object target, Class<?> clazz) throws Exception {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(MyAutowired.class)) {
                Class<?> fieldType = field.getType();
                Object dependency = getOrCreateBean(fieldType);
                field.setAccessible(true);
                field.set(target, dependency);
            }
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beanMap.get(clazz));
    }
}

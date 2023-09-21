package org.example.DILibrary;

import org.example.DILibrary.annotation.Controller;
import org.example.DILibrary.annotation.MyAutowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class MyContainer {
    private Map<Class<?>, Object> beanMap = new HashMap<>();
    private List<Class<?>> registeredClasses = new ArrayList<>();

    public void scanAndRegisterControllers(String basePackage) throws Exception {
        List<Class<?>> classes = ClassScanner.findClassesWithAnnotation(basePackage, Controller.class);
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

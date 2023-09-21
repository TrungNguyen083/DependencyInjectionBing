package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyInjector {
    public static void injectDependencies(Object target, MyContainer container) {
        injectFields(target, container);
        injectConstructors(target, container);
    }

    private static void injectFields(Object target, MyContainer container){
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(MyAutowired.class))
                .forEach(field -> {
                    Class<?> fieldType = field.getType();
                    Object dependency = container.getBean(fieldType);

                    if (dependency != null) {
                        field.setAccessible(true);
                        try {
                            field.set(target, dependency);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private static void injectConstructors(Object target, MyContainer container) {
        Class<?> clazz = target.getClass();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(MyAutowired.class)) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] dependencies = new Object[parameterTypes.length];

                boolean allDependenciesAvailable = true;

                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> paramType = parameterTypes[i];
                    Object dependency = container.getBean(paramType);

                    if (dependency != null) {
                        dependencies[i] = dependency;
                    } else {
                        allDependenciesAvailable = false;
                        break; // Exit the loop if any dependency is missing
                    }
                }

                if (allDependenciesAvailable) {
                    try {
                        constructor.setAccessible(true);
                        Object newInstance = constructor.newInstance(dependencies);
                        // Replace the original target with the new instance
                        target = newInstance;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectMethods(Object target, MyContainer container) {
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(MyAutowired.class))
                .forEach(method -> {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    List<Object> dependencies = Arrays.stream(parameterTypes)
                            .map(container::getBean)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    method.setAccessible(true);
                    try {
                        method.invoke(target, dependencies.toArray());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}

package org.example.dilibrary;

import org.example.dilibrary.annotation.MyAutowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class MyInjector {
    private MyInjector() {}
    public static void injectDependencies(Object target, MyContainer container) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        injectFields(target, container);
        injectConstructors(target, container);
    }

    private static void injectFields(Object target, MyContainer container) {
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

    private static Object injectConstructors(Object target, MyContainer container) throws InvocationTargetException, InstantiationException, IllegalAccessException {
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

                    if (dependency == null) {
                        allDependenciesAvailable = false;
                        break; // Exit the loop if any dependency is missing
                    }
                    dependencies[i] = dependency;
                }

                if (allDependenciesAvailable) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(dependencies);
                }
            }
        }
        return target;
    }
}

package org.example;

import java.util.HashMap;
import java.util.Map;

public class MyContainer {
    private Map<Class<?>, Object> beanMap = new HashMap<>();

    public void registerBean(Class<?> clazz) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();
        beanMap.put(clazz, instance);
    }

    public <T> T getBean(Class<T> clazz) {
        return clazz.cast(beanMap.get(clazz));
    }
}

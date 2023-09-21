package org.example.DILibrary;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> findClassesWithAnnotation(String basePackage, Class<? extends Annotation> annotation) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        String path = basePackage.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists()) {
                    String[] files = directory.list();
                    if (files != null) {
                        for (String fileName : files) {
                            if (fileName.endsWith(".class")) {
                                String className = basePackage + "." + fileName.substring(0, fileName.length() - 6);
                                Class<?> clazz = Class.forName(className);

                                if (clazz.isAnnotationPresent(annotation)) {
                                    annotatedClasses.add(clazz);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return annotatedClasses;
    }
}

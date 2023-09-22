package org.example.DILibrary;

import org.example.DILibrary.configuaration.ControllerConfig;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> findClassesWithAnnotation(
            String basePackage, Class<? extends Annotation> annotationClass, ControllerConfig controllerConfig) throws Exception {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        File packageDirectory = new File(basePackage);

        if (packageDirectory.exists() && packageDirectory.isDirectory()) {
            findAnnotatedClassesInDirectory(packageDirectory, basePackage, annotationClass, annotatedClasses, controllerConfig);
        }

        return annotatedClasses;
    }

    private static void findAnnotatedClassesInDirectory(
            File directory, String packageName, Class<? extends Annotation> annotationClass,
            List<Class<?>> annotatedClasses, ControllerConfig controllerConfig) throws Exception {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively search subdirectories
                String subPackageName = packageName + "\\" + file.getName();
                findAnnotatedClassesInDirectory(file, subPackageName, annotationClass, annotatedClasses, controllerConfig);
            } else if (file.getName().endsWith(controllerConfig.getFileExtension())) {
                // Handle class files
                String className = controllerConfig.getForNamePackage() + file.getName().replace(controllerConfig.getFileExtension(), "");
                Class<?> clazz = Class.forName(className);
                Annotation annotation = clazz.getAnnotation(annotationClass);
                if (annotation != null) {
                    annotatedClasses.add(clazz);
                }
            }
        }
    }
}

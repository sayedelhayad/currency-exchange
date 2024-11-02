package com.ce.bill.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

import org.reflections.Reflections;

public class ReflectionUtils {
    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classes = new HashSet<>();
        String packageName = "com";
        try {
            String path = packageName.replace('.', '/');
            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
            if (resource != null) {
                File directory = new File(resource.toURI());
                extracted(annotationClass, directory, packageName, classes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void extracted(final Class<? extends Annotation> annotationClass, final File directory, final String packageName, final Set<Class<?>> classes) throws ClassNotFoundException {

        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if(file.isFile()) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(packageName + '.' + className);
                    if (clazz.isAnnotationPresent(annotationClass)) {
                        classes.add(clazz);
                    }
                } else {
                    String subPackageName = packageName + "." + file.getName();
                    extracted(annotationClass, file, subPackageName, classes);
                }
            }
        }
    }


    public static List<Class<?>> loadDiscounts(Class<? extends Annotation> annotationClass) throws Exception {
        List<Class<?>> applicableDiscounts = new ArrayList<>();

        // Use reflection to scan all classes in the package
        Reflections reflections = new Reflections("com");

        for (Class<?> cls : reflections.getTypesAnnotatedWith(annotationClass)) {
            if (cls.isAnnotationPresent(annotationClass) && !Modifier.isAbstract(cls.getModifiers())) {
                Annotation discount = cls.getAnnotation(annotationClass);
                    try {
                        // Instantiate the instance class
                        Class<?> clazz = Class.forName(cls.getName());
                        applicableDiscounts.add(clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        }
        return applicableDiscounts;
    }

}

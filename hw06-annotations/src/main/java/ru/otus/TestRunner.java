package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class TestRunner {


    public static void testClass(Class<?> testClass) {
        int tests = 0, passed = 0, failed = 0;
        Object instance = instantiate(testClass);

        for (var method : testClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Test.class)) continue;

            tests++;

            try {
                var beforeMethod = getMethodWithAnnotation(testClass, Before.class);

                if (Objects.nonNull(beforeMethod)) {
                    beforeMethod.invoke(instance);
                }

                method.invoke(instance);
                System.out.println(method.getName() + " passed");
                passed++;
            } catch (Exception e) {
                failed++;
                System.out.println(method.getName() + " failed");


            } finally {
                var afterMethod = getMethodWithAnnotation(testClass, After.class);

                try {
                    if (Objects.nonNull(afterMethod)) {
                        afterMethod.invoke(instance);
                        System.out.println("--------------------------");
                    }
                } catch (IllegalAccessException | InvocationTargetException ignored) {

                }
            }
        }

        System.out.println("Tests run: " + tests + ", Passed: " + passed + ", Failed: " + failed);
    }

    private static Object instantiate(Class<?> testClass) {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getMethodWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }

        return null;
    }

}
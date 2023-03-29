package ru.otus;


import ru.otus.Annotation.After;
import ru.otus.Annotation.Before;
import ru.otus.Annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {


    public static void testClass(Class<?> testClass) {
        var instance = instantiate(testClass);
        int tests = 0, passed = 0, failed = 0;
        for (var method : testClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Test.class)) continue;

            tests++;

            var methodName = method.getName();
            var beforeMethod = getMethodWithAnnotation(testClass, Before.class);

            try {
                beforeMethod.invoke(instance);
                method.invoke(instance);
                System.out.println(methodName + " passed");
                passed++;
            } catch (Exception e) {
                failed++;
                System.out.println(methodName + " failed");
            } finally {
                var afterMethod = getMethodWithAnnotation(testClass, After.class);

                try {
                    afterMethod.invoke(instance);
                    System.out.println("--------------------------");
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

    private static Method getMethodWithAnnotation(Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }

        throw new RuntimeException(clazz.getSimpleName() + " should be marked at least one of @Before, @After, @Test annotations");
    }

}
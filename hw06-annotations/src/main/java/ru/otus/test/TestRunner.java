package ru.otus.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner implements Runner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    private final Class<?> testClass;
    private final Resultable<String, Integer> results;
    private final Map<Class<? extends Annotation>, List<Method>> testClassMethods = new HashMap<>() {{
        put(Test.class, new ArrayList<>());
        put(Before.class, new ArrayList<>());
        put(After.class, new ArrayList<>());
    }};
    private Object testInstance;

    public TestRunner(Class<?> testClass, Resultable<String, Integer> results) {
        this.testClass = testClass;
        this.results = results;

        groupMethodsIntoMap();
        validateMethods();
    }

    @Override
    public void run() {
        instantiateTestClass();
        logger.info("New instance of test class created: {}", testInstance);

        try {
            invokeSingleMethod(testClassMethods.get(Before.class).get(0));
            runTests(testClassMethods.get(Test.class));
        } finally {
            invokeSingleMethod(testClassMethods.get(After.class).get(0));
            results.print();
        }
    }

    private void groupMethodsIntoMap() {
        for (var method : testClass.getDeclaredMethods()) {
            for (var anno : testClassMethods.keySet()) {
                if (method.isAnnotationPresent(anno)) testClassMethods.get(anno).add(method);
            }
        }

        results.setValue(Values.STATS_TEST_KEY.getValue(), testClassMethods.get(Test.class).size());
    }

    private void validateMethods() {
        testClassMethods.forEach((annotation, val) -> {
            if (val.isEmpty()) {
                throw new RuntimeException("At least one method of the test class must be marked with an annotation @ " + annotation);
            }
        });
    }

    private void instantiateTestClass() {
        try {
            testInstance = testClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeSingleMethod(Method method) {
        try {
            logger.info("Calling {} method", method.getDeclaredAnnotations()[0]);
            method.invoke(testInstance);
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private void runTests(List<Method> methods) {
        for (var method : methods) {
            try {
                method.invoke(testInstance);
                results.incrementValue(Values.STATS_PASSED_KEY.getValue());
                logger.info("{}: {}", method, Values.STATS_PASSED_KEY.getValue());
            } catch (Exception e) {
                results.incrementValue(Values.STATS_FAILED_KEY.getValue());
                logger.info("{}: {}", method, Values.STATS_FAILED_KEY.getValue());
            }
        }
    }

}
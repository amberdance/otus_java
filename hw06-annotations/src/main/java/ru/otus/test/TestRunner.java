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
    private final List<Class<? extends Annotation>> allowedAnnotations = List.of(Test.class, After.class, Before.class);
    private final Resultable<String, Integer> results;
    private Map<Class<? extends Annotation>, List<Method>> testClassMethods;
    private Object testInstance;

    public TestRunner(Class<?> testClass, Resultable<String, Integer> results) {
        this.testClass = testClass;
        this.results = results;

        initEmptyMapOfClassMethods();
        collectMethods();
        validateMethods();
    }

    private void initEmptyMapOfClassMethods() {
        testClassMethods = new HashMap<>();

        for (var anno : allowedAnnotations) {
            testClassMethods.put(anno, new ArrayList<>());
        }
    }

    private void validateMethods() {
        testClassMethods.forEach((annotation, val) -> {
            if (val.isEmpty()) {
                throw new RuntimeException("At least one method of the test class must be marked with an annotation @ " + annotation);
            }
        });
    }

    private void collectMethods() {
        for (var method : testClass.getDeclaredMethods()) {
            for (var anno : allowedAnnotations) {
                if (method.isAnnotationPresent(anno)) testClassMethods.get(anno).add(method);
            }
        }

        results.setValue(Values.STATS_TEST_KEY.getValue(), testClassMethods.get(Test.class).size());
    }


    private void instantiateTestClass() {
        try {
            testInstance = testClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        instantiateTestClass();
        logger.info("New instance of test class created: " + testInstance);

        try {
            runBefore();
            runTests();
        } finally {
            runAfter();
            results.print();
        }
    }


    private void runAfter() {
        var afterMethod = testClassMethods.get(After.class).get(0);

        try {
            logger.info("Calling @After method: {}", afterMethod);
            testClassMethods.get(After.class).get(0).invoke(testInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void runTests() {
        for (var method : testClassMethods.get(Test.class)) {
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

    private void runBefore() {
        var beforeFirstMethod = testClassMethods.get(Before.class).get(0);

        try {
            logger.info("Calling @Before method: {}", beforeFirstMethod);
            beforeFirstMethod.invoke(testInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
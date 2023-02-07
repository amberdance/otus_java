package ru.otus.test;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;
import ru.otus.assertions.AssertionError;
import ru.otus.utils.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestRunner implements Runner {
    private final MethodContainer methodContainer;
    private final Object testInstance;

    public TestRunner(Object testInstance, MethodContainer methodContainer) {
        this.testInstance = testInstance;
        this.methodContainer = methodContainer;
    }

    @Override
    public void run() {
        runTests();
    }

    private void runTests() {
        runSetUp();

        Method[] testMethods = getTestMethods();
        int failedCount = 0;

        for (Method testCase : testMethods) {
            String methodSimpleName = testCase.getName();

            try {
                Reflection.invokeMethod(testCase, testInstance);
                TestLogger.log(String.format(TestLogger.LogFormat.PASSED_TEST_FORMAT.value(), methodSimpleName));
            } catch (InvocationTargetException | IllegalAccessException e) {
                Throwable cause = e.getCause();

                if (cause instanceof AssertionError) {
                    TestLogger.log(String.format(TestLogger.LogFormat.FAILED_TEST_FORMAT.value(), methodSimpleName,
                            cause.getMessage()));
                    failedCount++;
                }
            }
        }

        int totalCount = testMethods.length;
        TestLogger.log(String.format(TestLogger.LogFormat.STATISTICS_FORMAT.value(), totalCount,
                totalCount - failedCount, failedCount), 0);

        runTearDown();
    }

    private void runSetUp() {
        Method[] methods = methodContainer.getMethodByAnnotation(Before.class);
        TestController.failFast(methods.length > 1, Initializer.ErrorMessage.BEFORE_MUST_BE_DECLARED_ONCE.value());

        try {
            Reflection.invokeMethod(methods[0], testInstance);
        } catch (InvocationTargetException | IllegalAccessException e) {
            runTearDown();
        }
    }

    private Method[] getTestMethods() {
        Method[] testMethods = methodContainer.getMethodByAnnotation(Test.class);
        TestController.failFast(testMethods.length == 0, Initializer.ErrorMessage.NO_TESTS_DECLARED.value());

        return testMethods;
    }

    private void runTearDown() {
        Method[] methods = methodContainer.getMethodByAnnotation(After.class);
        TestController.failFast(methods.length > 1, Initializer.ErrorMessage.AFTER_MUST_BE_DECLARED_ONCE.value());

        try {
            Reflection.invokeMethod(methods[0], testInstance);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}

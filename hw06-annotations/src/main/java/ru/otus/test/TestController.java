package ru.otus.test;

import ru.otus.utils.Reflection;

public class TestController implements Initializer {

    private final MethodContainer methodContainer;
    private final Object testInstance;


    public TestController(Class<?> type) {
        testInstance = Reflection.instantiate(type);
        methodContainer = new MethodContainer(testInstance);
    }

    static void failFast(boolean condition, String message) {
        if (condition) throw new RuntimeException(message);
    }

    @Override
    public void init() {
        new TestMethodsInitializer(methodContainer).init();
        new TestRunner(testInstance, methodContainer).run();

        TestLogger.printLogs();
        TestLogger.clearLog();
    }

}

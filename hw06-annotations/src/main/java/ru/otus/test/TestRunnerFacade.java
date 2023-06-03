package ru.otus.test;

public class TestRunnerFacade {
    private TestRunnerFacade() {
        throw new UnsupportedOperationException();
    }

    public static void runTest(Class<?> theClass) {
        Resultable<String, Integer> results = new TestResult();
        Runner runner = new TestRunner(theClass, results);
        runner.run();
    }

}

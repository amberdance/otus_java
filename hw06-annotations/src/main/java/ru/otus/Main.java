package ru.otus;

import ru.otus.test.TestClass;
import ru.otus.test.TestRunnerFacade;

public class Main {

    public static void main(String[] args) {
        TestRunnerFacade.runTest(TestClass.class);
    }
}

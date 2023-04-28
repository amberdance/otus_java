package ru.otus;

import ru.otus.test.PangramCheckerTest;
import ru.otus.test.TestRunnerFacade;

public class Main {

    public static void main(String[] args) {
        TestRunnerFacade.runTest(PangramCheckerTest.class);
    }
}

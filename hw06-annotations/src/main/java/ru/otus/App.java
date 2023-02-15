package ru.otus;

import ru.otus.test.TestController;

public class App {
    public static void main(String[] args) {
        new TestController(PangramCheckerTest.class).init();
    }
}

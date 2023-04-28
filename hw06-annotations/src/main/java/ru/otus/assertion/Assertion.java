package ru.otus.assertion;

public class Assertion {

    public static void assertTrue(boolean expected, boolean actual) {
        if (expected != actual) throw new AssertionError(expected, actual);
    }
}

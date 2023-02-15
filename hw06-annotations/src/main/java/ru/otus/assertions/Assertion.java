package ru.otus.assertions;

public class Assertion {

    public static void assertEquals(String expected, String actual) {
        if (!expected.equals(actual)) throw new AssertionError(expected, actual);
    }

    public static void assertTrue(boolean expected, boolean actual) {
        if (expected != actual) throw new AssertionError(expected, actual);
    }
}

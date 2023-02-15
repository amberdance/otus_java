package ru.otus.assertions;

public class AssertionError extends Error {
    private static final String FAIL_MESSAGE = "Expected: %s, Actual: %s";

    public AssertionError(String expected, String actual) {
        super(String.format(FAIL_MESSAGE, expected, actual));
    }

    public AssertionError(boolean expected, boolean actual) {
        super(String.format(FAIL_MESSAGE, expected, actual));
    }
}

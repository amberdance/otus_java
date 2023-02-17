package ru.otus.solid.exception;

public class CapacityExhaustException extends RuntimeException {

    public static final String defaultMessage = "Cannot withdrew %d. Total available: %d";

    public CapacityExhaustException(String message) {
        super(message);
    }
}

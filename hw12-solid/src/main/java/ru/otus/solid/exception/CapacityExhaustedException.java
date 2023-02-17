package ru.otus.solid.exception;

public class CapacityExhaustedException extends RuntimeException {

    public static final String defaultMessage = "Cannot withdrew %d. Total available: %d";

    public CapacityExhaustedException(String message) {
        super(message);
    }
}

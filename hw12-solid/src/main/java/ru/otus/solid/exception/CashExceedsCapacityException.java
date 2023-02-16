package ru.otus.solid.exception;

public class CashExceedsCapacityException extends RuntimeException {

    public static String cashExceedsMessage = "Cannot withdrew %d. Total available: %d";

    public CashExceedsCapacityException(String message) {
        super(message);
    }
}

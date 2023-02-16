package ru.otus.solid.exception;

public class AmountExceededException extends RuntimeException {

    public AmountExceededException(String message) {
        super(message);
    }
}

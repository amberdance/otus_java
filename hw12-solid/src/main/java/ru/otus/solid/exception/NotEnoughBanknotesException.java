package ru.otus.solid.exception;

public class NotEnoughBanknotesException extends RuntimeException {

    public NotEnoughBanknotesException() {
    }

    public NotEnoughBanknotesException(String message) {
        super(message);
    }
}

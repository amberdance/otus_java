package ru.otus.solid.exception;

public class NotEnoughBanknotesException extends Exception {

    public NotEnoughBanknotesException() {
    }

    public NotEnoughBanknotesException(String message) {
        super(message);
    }
}

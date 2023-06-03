package ru.otus.processor.homework;

public class EvenSecondException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "Exception throws on %d second";

    public EvenSecondException(int second) {
        super(String.format(MESSAGE_TEMPLATE, second));
    }
}
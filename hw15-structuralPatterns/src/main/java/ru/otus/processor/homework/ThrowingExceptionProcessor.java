package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class ThrowingExceptionProcessor implements Processor {
    private final SecondsProvider secondsProvider;

    public ThrowingExceptionProcessor(SecondsProvider secondsProvider) {
        this.secondsProvider = secondsProvider;
    }

    public ThrowingExceptionProcessor() {
        this(() -> LocalDateTime.now().getSecond());
    }

    @Override
    public Message process(Message message) {
        int currentSecondOfMinute = secondsProvider.getCurrentSecondOfMinute();

        if (currentSecondOfMinute % 2 == 0) {
            throw new EvenSecondException(currentSecondOfMinute);
        }

        return message;
    }
}

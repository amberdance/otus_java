package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class HandleField11And12Processor implements Processor {
    @Override
    public Message process(Message message) {
        if (message != null) {
            return message.toBuilder()
                    .field11(message.getField12())
                    .field12(message.getField11())
                    .build();
        }
        return null;
    }
}

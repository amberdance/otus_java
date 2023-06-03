package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandleField11And12ProcessorTest {
    @Test
    @DisplayName("Исходные значения полей сохранены корректно и заменены")
    public void exchangeTest() {
        Message message =
                new Message.Builder(1)
                        .field11("field11")
                        .field12("field12")
                        .build();


        var changedMessage = new HandleField11And12Processor().process(message);

        assertEquals(changedMessage.getField11(), message.getField12());
        assertEquals(changedMessage.getField12(), message.getField11());
    }
}
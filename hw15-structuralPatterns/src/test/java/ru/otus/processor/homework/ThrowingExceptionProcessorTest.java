package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowingExceptionProcessorTest {
    @Test
    @DisplayName("Без exception на нечетных секундах")
    public void notThrowsExceptionOnOddSecondTest() {
        assertDoesNotThrow(() -> new ThrowingExceptionProcessor(() -> 1).process(null));
    }

    @Test
    @DisplayName("Exception четных секундах")
    public void throwsExceptionOnEvenSecondTest() {
        assertThrows(EvenSecondException.class, () -> new ThrowingExceptionProcessor(() -> 2).process(null));
    }


}
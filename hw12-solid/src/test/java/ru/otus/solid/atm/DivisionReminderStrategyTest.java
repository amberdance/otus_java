package ru.otus.solid.atm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DivisionReminderStrategyTest {

    @Test
    void testDivisionWithRemainder() {
        var givenCash = 10950;
        var strategy = new DivisionByReminderStrategy(givenCash);
        var slots = strategy.optimize().getResult();

        assertEquals(slots.get(Banknote.N_5000), 2);
        assertEquals(slots.get(Banknote.N_500), 1);
        assertEquals(slots.get(Banknote.N_200), 2);
        assertEquals(slots.get(Banknote.N_50), 1);
    }
}
package ru.otus.solid.atm;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizationStrategyTest {

    @Test
    void testDivisionWithRemainder() {
        var givenCash = 10950;
        var strategy = new OptimizationStrategy(givenCash);
        Map<Nominal, Integer> slots = strategy.divisionWithRemainder().getResult();

        assertEquals(slots.get(Nominal.N_5000), 2);
        assertEquals(slots.get(Nominal.N_500), 1);
        assertEquals(slots.get(Nominal.N_200), 2);
        assertEquals(slots.get(Nominal.N_50), 1);
    }
}
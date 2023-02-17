package ru.otus.solid.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.BanknoteSlot;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SunshineATMBanknotesTest {

    private static BanknoteSlot slots;

    @BeforeAll
    static void setup() {
        slots = new SunshineATMBanknotes();
    }

    @Test
    void givenEachNominal_whenTakeCash_thenSlotsCountShouldDecreased() throws NotEnoughBanknotesException {
        for (Nominal nominal : Nominal.values()) {
            var countOfBanknotesBefore = slots.getCountByNominal(nominal);
            slots.take(nominal, slots.DEFAULT_NOMINAL_COUNT);

            var countOfBanknotesAfter = slots.getCountByNominal(nominal);
            assertEquals(0, countOfBanknotesAfter);
        }
    }

    @Test
    void givenEachNominal_whenPutCash_thenSlotsCountShouldIncreased() {
        var cash = 500;

        for (Nominal nominal : Nominal.values()) {
            var countOfBanknotesBefore = slots.getCountByNominal(nominal);
            var countOfBanknotesAfter = mockPutBanknotes(nominal, cash);

            assertEquals(countOfBanknotesBefore, countOfBanknotesAfter - cash);
        }
    }

    private int mockPutBanknotes(Nominal nominal, int count) {
        var slots = new SunshineATMBanknotes();
        slots.put(nominal, count);

        return slots.getCountByNominal(nominal);
    }

    @Test
    void givenEachNominal_whenTakeMoreThatAvailable_thenThrowException() {
        var realBigCash = slots.getTotalSum() * 3;

        for (Nominal nominal : Nominal.values()) {
            assertThrows(Exception.class, () -> slots.take(nominal, realBigCash));
        }
    }

    @Test
    void givenEachNominal_whenTakeAll_thenReturnsZero() {
        for (Nominal nominal : Nominal.values()) {
            assertEquals(0, mockTakeBanknotes(nominal, slots.getCountByNominal(nominal)));
        }
    }

    private int mockTakeBanknotes(Nominal nominal, int count) {
        var slots = new SunshineATMBanknotes();

        try {
            slots.take(nominal, count);
            return slots.getCountByNominal(nominal);
        } catch (NotEnoughBanknotesException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Test
    void testTakeCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedOperationException.class, () -> slots.take(getRandomNominal(), 0));
    }

    private Nominal getRandomNominal() {
        return Nominal.class.getEnumConstants()[new Random().nextInt(Nominal.values().length - 1)];
    }

    @Test
    void testPutCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedOperationException.class, () -> slots.put(getRandomNominal(), 0));
    }

    @Test
    void testTotalCount() {
        assertEquals(BanknoteSlot.DEFAULT_NOMINAL_COUNT, slots.getTotalSlots() / Nominal.values().length);
    }
}
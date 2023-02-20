package ru.otus.solid.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.solid.common.Banknote;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.BanknoteSlots;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SunshineATMBanknoteSlotsTest {

    private static BanknoteSlots slots;

    @BeforeAll
    static void setup() {
        slots = new SunshineATMBanknoteSlots();
    }

    @Test
    void givenEachNominal_whenTakeCash_thenSlotsCountShouldDecreased() throws NotEnoughBanknotesException {
        for (var banknote : Banknote.values()) {
            slots.withdrawBanknotes(banknote, slots.DEFAULT_NOMINAL_COUNT);

            var countOfBanknotesAfter = slots.getCountByNominal(banknote);
            assertEquals(0, countOfBanknotesAfter);
        }
    }

    @Test
    void givenEachNominal_whenPutCash_thenSlotsCountShouldIncreased() {
        var count = 500;

        for (Banknote banknote : Banknote.values()) {
            var countOfBanknotesBefore = slots.getCountByNominal(banknote);
            var countOfBanknotesAfter = mockPutBanknotes(banknote, count);

            assertEquals(countOfBanknotesBefore, countOfBanknotesAfter - count);
        }
    }

    private int mockPutBanknotes(Banknote banknote, int count) {
        var slots = new SunshineATMBanknoteSlots();
        slots.depositBanknotes(banknote, count);

        return slots.getCountByNominal(banknote);
    }

    @Test
    void givenEachNominal_whenTakeMoreThatAvailable_thenThrowException() {
        var realBigCash = slots.getTotalSum() * 3;

        for (Banknote banknote : Banknote.values()) {
            assertThrows(Exception.class, () -> slots.withdrawBanknotes(banknote, realBigCash));
        }
    }

    @Test
    void givenEachNominal_whenTakeAll_thenReturnsZero() {
        for (Banknote banknote : Banknote.values()) {
            assertEquals(0, mockTakeBanknotes(banknote, slots.getCountByNominal(banknote)));
        }
    }

    private int mockTakeBanknotes(Banknote banknote, int count) {
        var slots = new SunshineATMBanknoteSlots();

        try {
            slots.withdrawBanknotes(banknote, count);
            return slots.getCountByNominal(banknote);
        } catch (NotEnoughBanknotesException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Test
    void testTakeCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedOperationException.class, () -> slots.withdrawBanknotes(getRandomNominal(), 0));
    }

    private Banknote getRandomNominal() {
        return Banknote.class.getEnumConstants()[new Random().nextInt(Banknote.values().length - 1)];
    }

    @Test
    void testPutCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedOperationException.class, () -> slots.depositBanknotes(getRandomNominal(), 0));
    }

    @Test
    void testTotalCount() {
        assertEquals(BanknoteSlots.DEFAULT_NOMINAL_COUNT, slots.getTotalSlots() / Banknote.values().length);
    }
}
package ru.otus.solid.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.exception.UnsupportedBanknoteException;
import ru.otus.solid.interfaces.BanknoteSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SunshineATMSlotsTest {

    private static BanknoteSlot slots;

    @BeforeAll
    static void setup() {
        slots = new SunshineATMBanknotes();
    }

    @Test
    void givenEachNominal_whenTakeCash_thenSlotsCountShouldDecrease() {
        var takenCount = 500;

        // 100
        var countOfNominalBefore = slots.getCountByNominal(Nominal.NOMINAL_100);
        var countOfNominalAfter = mockTakeBanknotes(Nominal.NOMINAL_100, takenCount);

        assertEquals(countOfNominalBefore, countOfNominalAfter + takenCount);

        // 500
        countOfNominalBefore = slots.getCountByNominal(Nominal.NOMINAL_500);
        countOfNominalAfter = mockTakeBanknotes(Nominal.NOMINAL_500, takenCount);

        assertEquals(countOfNominalBefore, countOfNominalAfter + takenCount);

        // 1000
        countOfNominalBefore = slots.getCountByNominal(Nominal.NOMINAL_1000);
        countOfNominalAfter = mockTakeBanknotes(Nominal.NOMINAL_1000, takenCount);

        assertEquals(countOfNominalBefore, countOfNominalAfter + takenCount);

        // 5000
        countOfNominalBefore = slots.getCountByNominal(Nominal.NOMINAL_5000);
        countOfNominalAfter = mockTakeBanknotes(Nominal.NOMINAL_5000, takenCount);

        assertEquals(countOfNominalBefore, countOfNominalAfter + takenCount);

    }


    private int mockTakeBanknotes(Nominal nominal, int count) {
        var slots = new SunshineATMBanknotes();

        try {
            slots.take(nominal, count);
        } catch (NotEnoughBanknotesException ignored) {
        }

        return slots.getCountByNominal(nominal);
    }

    @Test
    void givenEachNominal_whenPutCash_thenSlotsCountShouldIncrease() {
        var putCount = 500;

        // 100
        var countOnfSlotsBefore = slots.getCountByNominal(Nominal.NOMINAL_100);
        var countOfSlotsAfter = mockPutBanknotes(Nominal.NOMINAL_100, putCount);

        assertEquals(countOfSlotsAfter, countOnfSlotsBefore + putCount);

        // 500
        countOnfSlotsBefore = slots.getCountByNominal(Nominal.NOMINAL_500);
        countOfSlotsAfter = mockPutBanknotes(Nominal.NOMINAL_500, putCount);

        assertEquals(countOfSlotsAfter, countOnfSlotsBefore + putCount);

        // 1000
        countOnfSlotsBefore = slots.getCountByNominal(Nominal.NOMINAL_1000);
        countOfSlotsAfter = mockPutBanknotes(Nominal.NOMINAL_1000, putCount);

        assertEquals(countOfSlotsAfter, countOnfSlotsBefore + putCount);

        // 5000
        countOnfSlotsBefore = slots.getCountByNominal(Nominal.NOMINAL_5000);
        countOfSlotsAfter = mockPutBanknotes(Nominal.NOMINAL_5000, putCount);

        assertEquals(countOfSlotsAfter, countOnfSlotsBefore + putCount);
    }

    private int mockPutBanknotes(Nominal nominal, int count) {
        var slots = new SunshineATMBanknotes();
        slots.put(nominal, count);

        return slots.getCountByNominal(nominal);
    }

    @Test
    void givenEachNominal_whenTakeMoreThatAvailable_thenThrowException() {
        var largeCount = slots.getTotalSlots() * 2;

        assertThrows(NotEnoughBanknotesException.class, () -> slots.take(Nominal.NOMINAL_100, largeCount));
        assertThrows(NotEnoughBanknotesException.class, () -> slots.take(Nominal.NOMINAL_500, largeCount));
        assertThrows(NotEnoughBanknotesException.class, () -> slots.take(Nominal.NOMINAL_1000, largeCount));
        assertThrows(NotEnoughBanknotesException.class, () -> slots.take(Nominal.NOMINAL_5000, largeCount));

    }

    @Test
    void givenEachNominal_whenTakeAll_thenReturnsZero() {
        assertEquals(0, mockTakeBanknotes(Nominal.NOMINAL_100, slots.getCountByNominal(Nominal.NOMINAL_100)));
        assertEquals(0, mockTakeBanknotes(Nominal.NOMINAL_500, slots.getCountByNominal(Nominal.NOMINAL_500)));
        assertEquals(0, mockTakeBanknotes(Nominal.NOMINAL_1000, slots.getCountByNominal(Nominal.NOMINAL_1000)));
        assertEquals(0, mockTakeBanknotes(Nominal.NOMINAL_5000, slots.getCountByNominal(Nominal.NOMINAL_5000)));
    }

    @Test
    void testTakeCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedBanknoteException.class, () -> slots.take(Nominal.NOMINAL_5000, 0));
    }

    @Test
    void testPutCannotAcceptZeroBanknotes() {
        assertThrows(UnsupportedBanknoteException.class, () -> slots.put(Nominal.NOMINAL_5000, 0));
    }
}
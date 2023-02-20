package ru.otus.solid.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.solid.common.Banknote;
import ru.otus.solid.exception.CapacityExhaustedException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SunshineATMTest {

    private static SunshineATM sunshineATM;


    @BeforeAll
    static void setup() {
        sunshineATM = new SunshineATM();
    }


    @Test
    void givenATM_whenBooted_thenBalanceShouldEqualsInitialCapacity() {
        var atm = new SunshineATM();
        assertEquals(atm.getBanknoteSlots().getTotalSum(), atm.requestBalance());
    }


    @Test
    void givenEachNominalBanknote_whenDeposited_thenBalanceWillIncrease() {
        var balanceBeforeDeposit = sunshineATM.requestBalance();
        var profit = Arrays.stream(Banknote.values()).mapToInt(Banknote::value).sum();

        sunshineATM.requestDeposit(Banknote.values());

        var balanceAfterDeposit = sunshineATM.requestBalance();

        assertEquals(profit, balanceAfterDeposit - balanceBeforeDeposit);
    }

    @Test
    void givenSomeCash_whenTakeCash_thenBalanceWillDecrease() {
        var balanceBeforeDeposit = sunshineATM.requestBalance();
        var cash = balanceBeforeDeposit % Banknote.N_5000.value();

        sunshineATM.requestWithdraw(cash);

        var balanceAfterDeposit = sunshineATM.requestBalance();

        assertEquals(cash, balanceBeforeDeposit - balanceAfterDeposit);
    }


    @Test
    void givenAmountOfCash_whenCapacityExceed_thenThrewException() {
        assertThrows(CapacityExhaustedException.class,
                () -> sunshineATM.requestWithdraw(sunshineATM.requestBalance() * 2));
    }

    @Test
    void givenNotMultipleSum_whenTakeCash_thenThrewException() {
        assertThrows(IllegalArgumentException.class, () -> sunshineATM.requestWithdraw(66666));
    }

    @Test
    void givenDifferentSums_whenTakeCash_thenBalanceWillDecrease() {
        int[] cashCases = new int[]{50, 100, 200, 500, 900, 1000, 2200, 5000, 6600};

        for (int cash : cashCases) {
            assertDoesNotThrow(() -> sunshineATM.requestWithdraw(cash));
        }
    }

    @Test
    void givenSomeCash_whenTakeCash_thenBanknoteSlotsDecreasedByAppliedOptimizationStrategy() {
        var cash = 5950; // (1) 5000 + (1) 500 + (2) 200 + (1) 50

        var banknotes = sunshineATM.getBanknoteSlots();
        var countOf50NominalBefore = banknotes.getCountByNominal(Banknote.N_50);
        var countOf200NominalBefore = banknotes.getCountByNominal(Banknote.N_200);
        var countOf500NominalBefore = banknotes.getCountByNominal(Banknote.N_500);
        var countOf5000NominalBefore = banknotes.getCountByNominal(Banknote.N_5000);

        sunshineATM.requestWithdraw(cash);

        var countOf50NominalAfter = banknotes.getCountByNominal(Banknote.N_50);
        var countOf200NominalAfter = banknotes.getCountByNominal(Banknote.N_200);
        var countOf500NominalAfter = banknotes.getCountByNominal(Banknote.N_500);
        var countOf5000NominalAfter = banknotes.getCountByNominal(Banknote.N_5000);

        assertEquals(countOf50NominalBefore, countOf50NominalAfter + 1);
        assertEquals(countOf200NominalBefore, countOf200NominalAfter + 2);
        assertEquals(countOf500NominalBefore, countOf500NominalAfter + 1);
        assertEquals(countOf5000NominalBefore, countOf5000NominalAfter + 1);
    }
}
package ru.otus.solid.atm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.solid.exception.CapacityExhaustException;
import ru.otus.solid.utils.AtmLogger;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SunshineATMTest {

    private static SunshineATM sunshineATM;
    private static List<ILoggingEvent> atmLogs;

    @BeforeAll
    static void setup() {
        var logger = (Logger) LoggerFactory.getLogger(AtmLogger.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();
        logger.addAppender(listAppender);

        atmLogs = listAppender.list;
        sunshineATM = new SunshineATM();
    }

    @Test
    void givenATM_whenBooted_thenShowMetaLogs() {
        assertTrue(atmLogs.size() >= 2);
    }

    @Test
    void givenATM_whenBooted_thenBalanceShouldEqualsInitialCapacity() {
        var atm = new SunshineATM();
        assertEquals(atm.getBanknotes().getTotalSum(), atm.getBalance());
    }

    @Test
    void givenFirmwareId_whenATMBooted_thenShowSameFirmwareId() {
        var firmwareId = sunshineATM.getMeta().getHardwareId();
        assertTrue(atmLogs.get(1).getFormattedMessage().contains(firmwareId));
    }


    @Test
    void givenEachNominalBanknote_whenDeposited_thenBalanceWillIncrease() {
        var balanceBeforeDeposit = sunshineATM.getBalance();
        var profit = Arrays.stream(Nominal.values()).mapToInt(Nominal::value).sum();

        sunshineATM.put(Nominal.values());

        var balanceAfterDeposit = sunshineATM.getBalance();

        assertEquals(profit, balanceAfterDeposit - balanceBeforeDeposit);
    }

    @Test
    void givenSomeCash_whenTakeCash_thenBalanceWillDecrease() {
        var balanceBeforeDeposit = sunshineATM.getBalance();
        var cash = balanceBeforeDeposit % Nominal.N_5000.value();

        sunshineATM.take(cash);

        var balanceAfterDeposit = sunshineATM.getBalance();

        assertEquals(cash, balanceBeforeDeposit - balanceAfterDeposit);
    }


    @Test
    void givenAmountOfCash_whenCapacityExceed_thenThrewException() {
        assertThrows(CapacityExhaustException.class, () -> sunshineATM.take(sunshineATM.getBalance() * 2));
    }

    @Test
    void givenNotMultipleSum_whenTakeCash_thenThrewException() {
        assertThrows(IllegalArgumentException.class, () -> sunshineATM.take(66666));
    }

    @Test
    void givenDifferentSums_whenTakeCash_thenBalanceWillDecrease() {
        int[] cashCases = new int[]{50, 100, 200, 500, 900, 1000, 2200, 5000, 6600};

        for (int cash : cashCases) {
            assertDoesNotThrow(() -> sunshineATM.take(cash));
        }
    }

    @Test
    @Disabled
    void givenSomeCash_whenTakeCash_thenBanknoteSlotNominalWilDecreaseByMinimalNominalsCount() {
        var cash = 5950; // (1) 5000 + (1) 500 + (2) 200 + (1) 50

        var banknotes = sunshineATM.getBanknotes();
        var countOf50NominalBefore = banknotes.getCountByNominal(Nominal.N_50);
        var countOf100NominalBefore = banknotes.getCountByNominal(Nominal.N_100);
        var countOf5000NominalBefore = banknotes.getCountByNominal(Nominal.N_5000);

        sunshineATM.take(cash);

        var countOf50NominalAfter = banknotes.getCountByNominal(Nominal.N_50);
        var countOf100NominalAfter = banknotes.getCountByNominal(Nominal.N_100);
        var countOf5000NominalAfter = banknotes.getCountByNominal(Nominal.N_5000);

        //assertEquals(countOf50NominalAfter, countOf50NominalBefore + 1);
        //assertEquals(countOf100NominalAfter, countOf100NominalBefore + 9);
        assertEquals(countOf5000NominalBefore, countOf5000NominalAfter + 1);
    }
}
package ru.otus.solid.atm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.solid.exception.CapacityExhaustException;
import ru.otus.solid.utils.AtmLogger;

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
        var profit = 6600;
        var balanceBeforeDeposit = sunshineATM.getBalance();
        sunshineATM.store(Nominal.NOMINAL_100, Nominal.NOMINAL_500, Nominal.NOMINAL_1000, Nominal.NOMINAL_5000);

        var balanceAfterDeposit = sunshineATM.getBalance();
        assertEquals(profit, balanceAfterDeposit - balanceBeforeDeposit);
    }

    @Test
    void givenSomeCash_whenWithdraw_thenBalanceWillDecrease() {
        var difference = 10000;
        var balanceBeforeDeposit = sunshineATM.getBalance();

        sunshineATM.take(difference);
        var balanceAfterDeposit = sunshineATM.getBalance();

        assertEquals(difference, balanceBeforeDeposit - balanceAfterDeposit);
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
    void givenMultipleSum_whenTakeCash_thenBalanceWillDecrease() {
        assertDoesNotThrow(() -> sunshineATM.take(100));
        assertDoesNotThrow(() -> sunshineATM.take(200));
        assertDoesNotThrow(() -> sunshineATM.take(500));
        assertDoesNotThrow(() -> sunshineATM.take(900));
        assertDoesNotThrow(() -> sunshineATM.take(1000));
        assertDoesNotThrow(() -> sunshineATM.take(5000));
        assertDoesNotThrow(() -> sunshineATM.take(6600));
    }

    @Test
    void given500Nominal_whenTakeCash_thenBanknoteSlot500NominalWilDecrease() {
        var countOfNominal100Before = sunshineATM.getBanknotes().getCountByNominal(Nominal.NOMINAL_500);
        var taken = 500;
        sunshineATM.take(500);
        assertEquals(countOfNominal100Before, sunshineATM.getBanknotes().getCountByNominal(Nominal.NOMINAL_500) - 4);
    }

}
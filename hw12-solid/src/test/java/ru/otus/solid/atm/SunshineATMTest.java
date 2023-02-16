package ru.otus.solid.atm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.solid.exception.CashExceedsCapacityException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.utils.AtmLogger;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SunshineATMTest {

    private static ATM sunshineATM;
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
    void givenEachNominalBanknote_whenDeposited_thenBalanceWillIncreaseProportional() {
        var profit = 6600;
        var balanceBeforeDeposit = sunshineATM.getBalance();
        sunshineATM.store(Nominal.NOMINAL_100, Nominal.NOMINAL_500, Nominal.NOMINAL_1000, Nominal.NOMINAL_5000);

        var balanceAfterDeposit = sunshineATM.getBalance();
        assertEquals(profit, balanceAfterDeposit - balanceBeforeDeposit);
    }

    @Test
    void givenSomeCash_whenWithdraw_thenBalanceWillDecreaseProportional() {
        var difference = 10000;
        var balanceBeforeDeposit = sunshineATM.getBalance();

        sunshineATM.take(difference);
        var balanceAfterDeposit = sunshineATM.getBalance();

        assertEquals(difference, balanceBeforeDeposit - balanceAfterDeposit);
    }


    @Test
    void givenAmountOfCash_whenCapacityExceed_thenThrewException() {
        assertThrows(CashExceedsCapacityException.class, () -> sunshineATM.take(sunshineATM.getBalance() * 2));
    }

}
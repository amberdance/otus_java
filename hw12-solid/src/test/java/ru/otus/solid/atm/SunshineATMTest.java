package ru.otus.solid.atm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ru.otus.solid.exception.CashExceedsCapacityException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
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
    void givenATM_whenBooted_showMetaLogs() {
        assertTrue(atmLogs.size() >= 2);
    }

    @Test
    void givenFirmwareId_whenATMIBooted_showSameFirmwareId() {
        var firmwareId = sunshineATM.getMeta().getHardwareId();
        assertTrue(atmLogs.get(1).getFormattedMessage().contains(firmwareId));
    }

    @Test
    void givenATM_whenBooted_balanceEqualsInitialCapacity() {
        assertEquals(Balance.CAPACITY, new SunshineATM().getBalance());
    }

    @Test
    void givenSomeCash_whenDeposited_balanceWillIncreaseSameExact() {
        var profit = 6000;
        var balanceBeforeDeposit = sunshineATM.getBalance();
        var balanceAfterDeposit = mockDeposit(profit);

        assertEquals(profit, balanceAfterDeposit - balanceBeforeDeposit);
    }

    private int mockDeposit(int profit) {
        sunshineATM.store(profit);
        return sunshineATM.getBalance();
    }

    @Test
    void givenSomeCash_whenWithdraw_balanceWillDecreaseSameExact() {
        var difference = 10000;
        var balanceBeforeDeposit = sunshineATM.getBalance();
        var balanceAfterDeposit = mockWithdraw(difference);

        assertEquals(difference, balanceBeforeDeposit - balanceAfterDeposit);
    }

    private int mockWithdraw(int cost) {
        sunshineATM.take(cost);
        return sunshineATM.getBalance();
    }

    @Test
    void givenSomeCash_whenExceedingCapacity_thenThrewAmountExceededException() {
        assertThrows(CashExceedsCapacityException.class, () -> sunshineATM.take(Balance.CAPACITY * 2));
    }
}
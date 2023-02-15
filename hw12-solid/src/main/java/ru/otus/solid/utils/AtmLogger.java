package ru.otus.solid.utils;

import lombok.extern.slf4j.Slf4j;
import ru.otus.solid.atm.ATMMeta;

@Slf4j
public final class AtmLogger {

    private final static String operationFormat = "*** %s %s: %d. Remains: %d ***";

    private AtmLogger() {
        throw new UnsupportedOperationException("Sorry, but not");
    }

    public static void logInitializing() {
        log.info("Initializing services... please be patient");
    }

    public static void logBooted(ATMMeta meta) {
        log.info(String.format("@%s corp.%n" + "--Version: %s%n" + "--SunshineATM id: %s%n" + "Call %s if you have a " +
                        "troubles" +
                        ".Have a nice day!", meta.getCorporation(), meta.getVersion(), meta.getHardwareId(),
                meta.getContactCenter()));
    }

    public static void logDeposit(int profit, int remains) {
        log.info(String.format(operationFormat, "(+)", "Deposited", profit, remains));

    }

    public static void logWithDraw(int cost, int remains) {
        log.info(String.format(operationFormat, "(-)", "Withdraw", cost, remains));
    }
}

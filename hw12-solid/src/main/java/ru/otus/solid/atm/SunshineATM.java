package ru.otus.solid.atm;

import lombok.*;
import ru.otus.solid.exception.AmountExceededException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
import ru.otus.solid.utils.AtmLogger;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class SunshineATM implements ATM {

    public static String CONTACT_CENTER = " 8-666-66-66";
    public static String VERSION = "1.02";
    @Setter(AccessLevel.NONE)
    private ATMMeta meta;
    private final Balance balance;


    public SunshineATM() {
        AtmLogger.logInitializing();

        this.balance = new SunshineATMBalance();
        this.meta =
                new ATMMeta.ATMMetaBuilder().corporation(getClass().getSimpleName()).contactCenter(CONTACT_CENTER).version(VERSION).hardwareId(UUID.randomUUID().toString()).build();

        AtmLogger.logBooted(meta);
    }


    @Override
    public int getBalance() {
        return balance.remains();
    }


    @Override
    public void store(int profit) {
        balance.deposit(profit);
        AtmLogger.logDeposit(profit, balance);
    }

    @Override
    public void take(int cost) {
        if (cost > balance.remains()) throw new AmountExceededException("The requested amount is not available at " +
                "the ATM");

        balance.withdraw(cost);
        AtmLogger.logWithDraw(cost, balance);
    }
}

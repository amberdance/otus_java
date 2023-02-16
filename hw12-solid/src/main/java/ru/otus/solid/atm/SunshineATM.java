package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.solid.exception.CashExceedsCapacityException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
import ru.otus.solid.interfaces.BanknoteSlot;
import ru.otus.solid.interfaces.Nominal;
import ru.otus.solid.utils.AtmLogger;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class SunshineATM implements ATM {

    public static String CONTACT_CENTER = " 8-666-66-66";
    public static String VERSION = "1.02";

    private final ATMMeta meta;
    private final BanknoteSlot banknotes;
    private final Balance balance;


    public SunshineATM() {
        AtmLogger.logInitializing();

        banknotes = new SunshineATMBanknotes();
        balance = new SunshineATMBalance(banknotes.getTotalSum());
        meta = new ATMMeta.ATMMetaBuilder().corporation(getClass().getSimpleName()).contactCenter(CONTACT_CENTER).version(VERSION).hardwareId(UUID.randomUUID().toString()).build();

        AtmLogger.logBooted(meta);
    }


    @Override
    public int getBalance() {
        return balance.remains();
    }

    @Override
    public void store(Nominal... nominals) {
        var profit = 0;

        for (Nominal nominal : nominals) {
            banknotes.put(nominal, 1);
            profit += nominal.representation();
        }

        balance.deposit(profit);
        AtmLogger.logDeposit(profit, balance);
    }

    @Override
    public void take(int cost) {
        int remains = balance.remains();

        if (cost > remains) {
            AtmLogger.logCashExceedsCapacity(cost, remains);
            throw new CashExceedsCapacityException(String.format(CashExceedsCapacityException.cashExceedsMessage,
                    cost, remains));
        }

        balance.withdraw(cost);
        AtmLogger.logWithdraw(cost, balance);
    }
}

package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.solid.exception.CapacityExhaustException;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
import ru.otus.solid.interfaces.BanknoteSlot;
import ru.otus.solid.interfaces.OptimizationStrategy;
import ru.otus.solid.utils.AtmLogger;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class SunshineATM implements ATM {

    public static String CONTACT_CENTER = "8-666-66-66";
    public static String VERSION = "1.03";
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
    public void put(Nominal... nominals) {
        var profit = 0;

        for (Nominal nominal : nominals) {
            banknotes.put(nominal, 1);
            profit += nominal.value();
        }

        balance.deposit(profit);
        AtmLogger.logDeposit(profit, balance);
    }

    @Override
    public void take(int cash) {
        validateOperation(cash);

        try {
            takeBanknotes(new DivisionByReminderStrategy(cash));
            balance.withdraw(cash);
            AtmLogger.logWithdraw(cash, balance);
        } catch (NotEnoughBanknotesException e) {
            e.printStackTrace();
        }
    }

    private void validateOperation(int cash) {
        if (!requestedCashDividedClearlyByMinimalNominal(cash))
            throw new IllegalArgumentException("Requested sum should clearly divided by " + Nominal.N_50.value());

        int remains = balance.remains();

        if (cash > remains) {
            AtmLogger.logExhaustMessage(cash, remains);
            throw new CapacityExhaustException(String.format(CapacityExhaustException.defaultMessage, cash, remains));
        }
    }

    private void takeBanknotes(OptimizationStrategy<Nominal, Integer> strategy) throws NotEnoughBanknotesException {
        var result = strategy.optimize().getResult();

        for (var entry : result.entrySet()) {
            try {
                banknotes.take(entry.getKey(), entry.getValue());
            } catch (NotEnoughBanknotesException e) {
                var requestedCash = entry.getKey().value() * entry.getValue();
                AtmLogger.logRequestedCountNotEnough(requestedCash);
                throw new NotEnoughBanknotesException();
            }
        }
    }

    private boolean requestedCashDividedClearlyByMinimalNominal(int cash) {
        return cash % Nominal.N_50.value() == 0;
    }
}


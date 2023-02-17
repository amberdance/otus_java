package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.solid.common.Banknote;
import ru.otus.solid.common.DivisionByReminderStrategy;
import ru.otus.solid.exception.CapacityExhaustedException;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
import ru.otus.solid.interfaces.BanknoteSlots;
import ru.otus.solid.interfaces.OptimizationStrategy;
import ru.otus.solid.utils.AtmLogger;

import java.util.Arrays;
import java.util.UUID;


@ToString
@Getter
@EqualsAndHashCode
public class SunshineATM implements ATM {

    public static final String CONTACT_CENTER = "8-666-666-666";
    public static final String VERSION = "1.05";
    private final AtmMeta meta;
    private final BanknoteSlots banknoteSlots;
    private final Balance balance;

    public SunshineATM() {
        AtmLogger.logInitializing();

        meta = new AtmMeta.AtmMetaBuilder().corporation(getClass().getSimpleName()).contactCenter(CONTACT_CENTER).version(VERSION).hardwareId(UUID.randomUUID().toString()).build();
        banknoteSlots = new SunshineATMBanknoteSlots();
        balance = new SunshineATMBalance(banknoteSlots.getTotalSum());

        AtmLogger.logBooted(meta);
    }

    @Override
    public int requestBalance() {
        return balance.remains();
    }

    @Override
    public void requestDeposit(Banknote... banknotes) {
        banknoteSlots.depositBanknotes(1, banknotes);
        var profit = Arrays.stream(banknotes).mapToInt(Banknote::value).sum();
        balance.deposit(profit);

        AtmLogger.logDeposit(profit, balance);
    }

    @Override
    public void requestWithdraw(int cash) {
        validateWithdraw(cash);

        try {
            takeBanknotes(new DivisionByReminderStrategy(cash));
            balance.withdraw(cash);
            AtmLogger.logWithdraw(cash, balance);
        } catch (NotEnoughBanknotesException e) {
            e.printStackTrace();
            AtmLogger.logError(e.getMessage());
        }
    }

    private void validateWithdraw(int cash) {
        if (!requestedCashDividedClearlyByMinimalNominal(cash))
            throw new IllegalArgumentException("Requested sum should clearly divided by " + Banknote.N_50.value());

        int remains = balance.remains();

        if (cash > remains) {
            AtmLogger.logExhaustMessage(cash, remains);
            throw new CapacityExhaustedException(String.format(CapacityExhaustedException.defaultMessage, cash,
                    remains));
        }
    }

    private void takeBanknotes(OptimizationStrategy<Banknote, Integer> strategy) throws NotEnoughBanknotesException {
        var result = strategy.optimize().getResult();

        for (var entry : result.entrySet()) {
            var nominal = entry.getKey();
            var banknotesCount = entry.getValue();

            try {
                banknoteSlots.withdrawBanknotes(nominal, banknotesCount);
            } catch (NotEnoughBanknotesException e) {
                var requestedCash = nominal.value() * banknotesCount;
                AtmLogger.logRequestedCountNotEnough(requestedCash);
                throw new NotEnoughBanknotesException();
            }
        }
    }

    private boolean requestedCashDividedClearlyByMinimalNominal(int cash) {
        return cash % Banknote.N_50.value() == 0;
    }
}


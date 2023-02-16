package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.solid.exception.CapacityExhaustException;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.ATM;
import ru.otus.solid.interfaces.Balance;
import ru.otus.solid.interfaces.BanknoteSlot;
import ru.otus.solid.utils.AtmLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        // TODO: 16.02.2023 maybe add another method to deal with map of nominals
        var profit = 0;

        for (Nominal nominal : nominals) {
            banknotes.put(nominal, 1);
            profit += nominal.value();
        }

        takeBanknotes(profit);
        balance.deposit(profit);
        AtmLogger.logDeposit(profit, balance);
    }

    @Override
    public void take(int cost) {
        if (cost % Nominal.NOMINAL_100.value() != 0)
            throw new IllegalArgumentException("Requested sum " + "should be multiple to " + Nominal.NOMINAL_100.value());

        int remains = balance.remains();

        if (cost > remains) {
            AtmLogger.logExhaustMessage(cost, remains);
            throw new CapacityExhaustException(String.format(CapacityExhaustException.defaultMessage, cost, remains));
        }

        takeBanknotes(cost);
        balance.withdraw(cost);
        AtmLogger.logWithdraw(cost, balance);
    }

    private void takeBanknotes(int cost) {
        Map<Nominal, Integer> banknotesTable = new HashMap<>();

        for (Nominal nominal : Nominal.values()) {
            banknotesTable.put(nominal, 0);
        }

        do {
            if (cost % 5000 == 0) {
                banknotesTable.put(Nominal.NOMINAL_5000, banknotesTable.get(Nominal.NOMINAL_5000) + 1);
                cost -= 5000;
                continue;
            }

            if (cost % 1000 == 0) {
                banknotesTable.put(Nominal.NOMINAL_1000, banknotesTable.get(Nominal.NOMINAL_1000) + 1);
                cost -= 1000;
                continue;
            }
            if (cost % 500 == 0) {
                banknotesTable.put(Nominal.NOMINAL_500, banknotesTable.get(Nominal.NOMINAL_500) + 1);
                cost -= 500;
                continue;
            }

            if (cost % 100 == 0) {
                banknotesTable.put(Nominal.NOMINAL_100, banknotesTable.get(Nominal.NOMINAL_100) + 1);
                cost -= 100;
            }

        } while (cost > 0);

        banknotesTable =
                banknotesTable.entrySet().stream().filter(entry -> entry.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(banknotesTable);

        for (Map.Entry<Nominal, Integer> entry : banknotesTable.entrySet()) {
            try {
                banknotes.take(entry.getKey(), entry.getValue());
            } catch (NotEnoughBanknotesException e) {
                AtmLogger.logRequestedBanknotesNotEnough();
                e.printStackTrace();
            }
        }
    }
}


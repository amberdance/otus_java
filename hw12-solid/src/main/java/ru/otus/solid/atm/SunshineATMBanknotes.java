package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.exception.UnsupportedBanknoteException;
import ru.otus.solid.interfaces.BanknoteSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode
@ToString
public class SunshineATMBanknotes implements BanknoteSlot {

    private static final int DEFAULT_NOMINAL_COUNT = 1000;
    private final HashMap<Nominal, Integer> banknotes = new HashMap<>();

    public SunshineATMBanknotes() {
        this.banknotes.put(Nominal.NOMINAL_100, DEFAULT_NOMINAL_COUNT);
        this.banknotes.put(Nominal.NOMINAL_500, DEFAULT_NOMINAL_COUNT);
        this.banknotes.put(Nominal.NOMINAL_1000, DEFAULT_NOMINAL_COUNT);
        this.banknotes.put(Nominal.NOMINAL_5000, DEFAULT_NOMINAL_COUNT);
    }

    @Override
    public void take(Nominal nominal, int requestedBanknotes) throws NotEnoughBanknotesException {
        if (requestedBanknotesNotPresent(requestedBanknotes)) throw new UnsupportedBanknoteException();
        if (requestedBanknotesExceed(requestedBanknotes, nominal)) throw new NotEnoughBanknotesException();

        banknotes.put(nominal, banknotes.get(nominal) - requestedBanknotes);
    }

    @Override
    public void put(Nominal nominal, int count) {
        if (requestedBanknotesNotPresent(count)) throw new UnsupportedBanknoteException();

        banknotes.put(nominal, banknotes.get(nominal) + count);
    }

    @Override
    public int getCountByNominal(Nominal nominal) {
        return banknotes.entrySet().stream().filter(entry -> entry.getKey().equals(nominal)).mapToInt(Map.Entry::getValue).findFirst().orElse(-1);
    }

    @Override
    public int getTotalSum() {
        AtomicInteger sum = new AtomicInteger();
        banknotes.forEach((key, value) -> sum.set(value * key.representation()));

        return sum.intValue();
    }

    @Override
    public int getTotalSlots() {
        return banknotes.values().stream().reduce(0, Integer::sum);
    }

    private boolean requestedBanknotesNotPresent(int count) {
        return count == 0;
    }

    private boolean requestedBanknotesExceed(int requested, Nominal nominal) {
        return requested > getCountByNominal(nominal);
    }

}

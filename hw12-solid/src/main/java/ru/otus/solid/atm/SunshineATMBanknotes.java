package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.BanknoteSlot;
import ru.otus.solid.interfaces.Nominal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
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
        if (requestedBanknotes == 0) throw new IllegalArgumentException();

        var remainsBanknotes = getCountByNominal(nominal);
        if (requestedBanknotes > remainsBanknotes) throw new NotEnoughBanknotesException();

        banknotes.put(nominal, banknotes.get(nominal) - requestedBanknotes);
    }

    @Override
    public void put(Nominal nominal, int count) {
        if (count == 0) throw new IllegalArgumentException();

        banknotes.put(nominal, banknotes.get(nominal) + count);
    }

    @Override
    public int getCountByNominal(Nominal nominal) {
        return banknotes.entrySet().stream().filter(entry -> entry.getKey().equals(nominal)).mapToInt(Map.Entry::getValue).findFirst().orElse(-1);
    }

    @Override
    public int getSumByNominal(Nominal nominal) {
        return banknotes.entrySet().stream().filter(entry -> entry.getKey().equals(nominal)).map(Map.Entry::getValue).reduce(0, Integer::sum);
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


}

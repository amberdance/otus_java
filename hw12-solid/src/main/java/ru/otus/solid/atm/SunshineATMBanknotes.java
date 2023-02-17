package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.exception.NotEnoughBanknotesException;
import ru.otus.solid.interfaces.BanknoteSlot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode
@ToString
public class SunshineATMBanknotes implements BanknoteSlot {

    private final Map<Nominal, Integer> slots = new LinkedHashMap<>();

    public SunshineATMBanknotes() {
        for (Nominal nominal : Nominal.values()) {
            slots.put(nominal, DEFAULT_NOMINAL_COUNT);
        }
    }

    @Override
    public void take(Nominal nominal, int requestedCount) throws NotEnoughBanknotesException {
        if (requestedCountIsZero(requestedCount))
            throw new UnsupportedOperationException("Count on banknotes must be" + " greater than zero");
        if (requestedCountExceed(nominal, requestedCount)) throw new NotEnoughBanknotesException();

        slots.put(nominal, slots.get(nominal) - requestedCount);
    }

    @Override
    public void put(Nominal nominal, int count) {
        if (requestedCountIsZero(count)) throw new UnsupportedOperationException();

        slots.put(nominal, slots.get(nominal) + count);
    }

    @Override
    public int getCountByNominal(Nominal nominal) {
        return slots.entrySet().stream().filter(entry -> entry.getKey().equals(nominal)).mapToInt(Map.Entry::getValue).findFirst().orElse(-1);
    }

    @Override
    public int getTotalSum() {
        AtomicInteger sum = new AtomicInteger();
        slots.forEach((nominal, count) -> sum.set(nominal.value() * count));

        return sum.intValue();
    }

    @Override
    public int getTotalSlots() {
        return slots.values().stream().mapToInt(Integer::valueOf).sum();
    }

    private boolean requestedCountIsZero(int count) {
        return count == 0;
    }

    private boolean requestedCountExceed(Nominal nominal, int count) {
        return count > getCountByNominal(nominal);
    }

}

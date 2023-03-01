package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.BanknoteCassette;
import ru.otus.solid.common.Banknote;
import ru.otus.solid.exception.NotEnoughBanknotesException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode
@ToString
public class SunshineATMBanknoteCassette implements BanknoteCassette {

    private final Map<Banknote, Integer> slots = new LinkedHashMap<>();

    public SunshineATMBanknoteCassette() {
        for (Banknote banknote : Banknote.values()) {
            slots.put(banknote, DEFAULT_NOMINAL_COUNT);
        }
    }

    @Override
    public void take(Banknote banknote, int requestedCount) {
        if (requestedCountIsZero(requestedCount))
            throw new UnsupportedOperationException("Count on banknotes must be" + " greater than zero");
        if (requestedCountExceed(banknote, requestedCount)) throw new NotEnoughBanknotesException();

        slots.put(banknote, slots.get(banknote) - requestedCount);
    }

    @Override
    public void put(Banknote banknote, int count) {
        validateDeposit(count);
        slots.put(banknote, slots.get(banknote) + count);
    }

    @Override
    public void put(int count, Banknote... banknotes) {
        validateDeposit(count);

        for (var banknote : banknotes) {
            slots.put(banknote, slots.get(banknote) + count);
        }
    }

    @Override
    public int getCountByNominal(Banknote banknote) {
        return slots.entrySet().stream().filter(entry -> entry.getKey().equals(banknote)).mapToInt(Map.Entry::getValue).findFirst().orElse(-1);
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

    private void validateDeposit(int count) {
        if (requestedCountIsZero(count)) throw new UnsupportedOperationException();
    }

    private boolean requestedCountIsZero(int count) {
        return count == 0;
    }

    private boolean requestedCountExceed(Banknote banknote, int count) {
        return count > getCountByNominal(banknote);
    }

}

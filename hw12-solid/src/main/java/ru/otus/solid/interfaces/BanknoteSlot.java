package ru.otus.solid.interfaces;

import ru.otus.solid.atm.Nominal;
import ru.otus.solid.exception.NotEnoughBanknotesException;

import java.util.HashMap;
import java.util.Map;

public interface BanknoteSlot {

    public static final int DEFAULT_NOMINAL_COUNT = 100;

    default Map<Nominal, Integer> getEmptySlots() {
        var slots = new HashMap<Nominal, Integer>();

        for (Nominal nominal : Nominal.values()) {
            slots.put(nominal, 0);
        }

        return slots;
    }

    void take(Nominal nominal, int count) throws NotEnoughBanknotesException;

    void put(Nominal nominal, int count);

    int getCountByNominal(Nominal nominal);

    int getTotalSum();

    int getTotalSlots();
}

package ru.otus.solid;

import ru.otus.solid.common.Banknote;

public interface BanknoteSlots {

    int DEFAULT_NOMINAL_COUNT = 100;

    void take(Banknote banknote, int count);

    void put(Banknote banknote, int count);

    void put(int count, Banknote... banknotes);

    int getCountByNominal(Banknote banknote);

    int getTotalSum();

    int getTotalSlots();
}

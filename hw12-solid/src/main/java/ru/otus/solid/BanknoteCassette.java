package ru.otus.solid;

import ru.otus.solid.common.Banknote;

public interface BanknoteCassette {

    int DEFAULT_NOMINAL_COUNT = 2500;

    void take(Banknote banknote, int count);

    void put(Banknote banknote, int count);

    void put(int count, Banknote... banknotes);

    int getCountByNominal(Banknote banknote);

    int getTotalSum();

    int getTotalSlots();
}

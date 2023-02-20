package ru.otus.solid;

import ru.otus.solid.common.Banknote;

public interface BanknoteSlots {

    int DEFAULT_NOMINAL_COUNT = 100;

    void withdrawBanknotes(Banknote banknote, int count);

    void depositBanknotes(Banknote banknote, int count);

    void depositBanknotes(int count, Banknote... banknotes);

    int getCountByNominal(Banknote banknote);

    int getTotalSum();

    int getTotalSlots();
}

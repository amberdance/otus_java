package ru.otus.solid.interfaces;

import ru.otus.solid.atm.Banknote;
import ru.otus.solid.exception.NotEnoughBanknotesException;

public interface BanknoteSlots {

    int DEFAULT_NOMINAL_COUNT = 100;

    void take(Banknote banknote, int count) throws NotEnoughBanknotesException;

    void put(Banknote banknote, int count);

    int getCountByNominal(Banknote banknote);

    int getTotalSum();

    int getTotalSlots();
}

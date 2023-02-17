package ru.otus.solid.interfaces;

import ru.otus.solid.atm.Banknote;
import ru.otus.solid.exception.NotEnoughBanknotesException;

public interface BanknoteSlots {

    int DEFAULT_NOMINAL_COUNT = 100;

    void withdrawBanknotes(Banknote banknote, int count) throws NotEnoughBanknotesException;

    void depositBanknotes(Banknote banknote, int count);

    void depositBanknotes(int count, Banknote... banknotes);

    int getCountByNominal(Banknote banknote);

    int getTotalSum();

    int getTotalSlots();
}

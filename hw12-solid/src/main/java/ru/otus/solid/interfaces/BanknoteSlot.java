package ru.otus.solid.interfaces;

import ru.otus.solid.atm.Nominal;
import ru.otus.solid.exception.NotEnoughBanknotesException;

public interface BanknoteSlot {

    void take(Nominal nominal, int count) throws NotEnoughBanknotesException;

    void put(Nominal nominal, int count);


    int getCountByNominal(Nominal nominal);

    int getTotalSum();

    int getTotalSlots();


}

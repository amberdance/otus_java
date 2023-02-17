package ru.otus.solid.interfaces;

import ru.otus.solid.atm.AtmMeta;
import ru.otus.solid.atm.Banknote;

public interface ATM {


    int getBalance();

    AtmMeta getMeta();

    void put(Banknote... banknotes);

    void take(int cash);

}

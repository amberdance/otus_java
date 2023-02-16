package ru.otus.solid.interfaces;

import ru.otus.solid.atm.ATMMeta;
import ru.otus.solid.atm.Nominal;

public interface ATM {


    int getBalance();

    ATMMeta getMeta();

    void put(Nominal... nominals);

    void take(int cash);

}

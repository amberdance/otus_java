package ru.otus.solid.interfaces;

import ru.otus.solid.atm.ATMMeta;

public interface ATM {


    int getBalance();

    ATMMeta getMeta();

    void store(Nominal... nominals);

    void take(int cost);

}

package ru.otus.solid.interfaces;

import ru.otus.solid.atm.ATMMeta;

public interface ATM {


    int getBalance();

    ATMMeta getMeta();

    void store(int profit);

    void take(int cost);

}

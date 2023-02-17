package ru.otus.solid.interfaces;

import ru.otus.solid.atm.AtmMeta;
import ru.otus.solid.atm.Banknote;

public interface ATM {

    AtmMeta getMeta();

    int requestBalance();

    void requestDeposit(Banknote... banknotes);

    void requestWithdraw(int cash);

}

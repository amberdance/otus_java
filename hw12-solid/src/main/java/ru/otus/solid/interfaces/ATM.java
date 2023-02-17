package ru.otus.solid.interfaces;

import ru.otus.solid.common.AtmMeta;
import ru.otus.solid.common.Banknote;

public interface ATM {

    AtmMeta getMeta();

    int requestBalance();

    void requestDeposit(Banknote... banknotes);

    void requestWithdraw(int cash);

}

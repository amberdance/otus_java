package ru.otus.solid;

import ru.otus.solid.common.Banknote;

public interface ATM {

    int requestBalance();

    void requestDeposit(Banknote... banknotes);

    void requestWithdraw(int cash);

}

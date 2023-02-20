package ru.otus.solid.atm;

import lombok.Getter;
import ru.otus.solid.ATM;
import ru.otus.solid.Balance;
import ru.otus.solid.BanknoteSlots;

@Getter
public abstract class BaseATM implements ATM {

    protected Balance balance;
    protected BanknoteSlots banknoteSlots;
    protected AtmMeta meta;

    @Override
    public int requestBalance() {
        return balance.remains();
    }

}

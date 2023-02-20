package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.Balance;


@EqualsAndHashCode
@ToString
public class SunshineATMBalance implements Balance {

    private int remains;

    public SunshineATMBalance(int initialCapacity) {
        this.remains = initialCapacity;
    }

    @Override
    public int remains() {
        return remains;
    }

    @Override
    public void deposit(int profit) {
        remains += profit;
    }

    @Override
    public void withdraw(int cost) {
        remains -= cost;
    }
}

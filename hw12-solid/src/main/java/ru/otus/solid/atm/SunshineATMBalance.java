package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.interfaces.Balance;


@EqualsAndHashCode
@ToString
public class SunshineATMBalance implements Balance {

    private int current;

    public SunshineATMBalance(int initialCapacity) {
        this.current = initialCapacity;
    }

    @Override
    public int remains() {
        return current;
    }

    @Override
    public void deposit(int profit) {
        current += profit;
    }

    @Override
    public void withdraw(int cost) {
        current -= cost;
    }
}

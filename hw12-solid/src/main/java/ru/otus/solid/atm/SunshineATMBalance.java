package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.exception.AmountExceededException;
import ru.otus.solid.interfaces.Balance;


@EqualsAndHashCode
@ToString
public class SunshineATMBalance implements Balance {

    private int current;

    public SunshineATMBalance() {
        this.current = CAPACITY;
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
        if (cost > current) throw new AmountExceededException("The requested amount is not available at " +
                "the ATM");
        
        current -= cost;
    }
}

package ru.otus.solid.atm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.solid.interfaces.Balance;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SunshineATMBalanceTest {

    private final static int INITIAL_CAPACITY = new Random().nextInt(500000, 1000000);
    private static Balance balance;

    @BeforeAll
    static void setup() {
        balance = new SunshineATMBalance(INITIAL_CAPACITY);
    }

    @Test
    void testInstantiate() {
        assertEquals(INITIAL_CAPACITY, new SunshineATMBalance(INITIAL_CAPACITY).remains());
    }

    @Test
    void testDeposit() {
        var balanceBefore = balance.remains();
        var profit = new Random().nextInt(INITIAL_CAPACITY);

        balance.deposit(profit);
        assertEquals(balanceBefore, balance.remains() - profit);
    }

    @Test
    void testWithdraw() {
        var balanceBefore = balance.remains();
        var cost = new Random().nextInt(INITIAL_CAPACITY);

        balance.withdraw(cost);
        assertEquals(balanceBefore, balance.remains() + cost);
    }
}
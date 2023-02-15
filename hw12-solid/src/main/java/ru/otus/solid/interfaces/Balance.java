package ru.otus.solid.interfaces;

public interface Balance {

    static final int CAPACITY = 1_000_000;

    int remains();

    void deposit(int profit);

    void withdraw(int cost);
}

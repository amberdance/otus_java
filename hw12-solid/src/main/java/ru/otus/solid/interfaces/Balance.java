package ru.otus.solid.interfaces;

public interface Balance {

    int remains();

    void deposit(int profit);

    void withdraw(int cost);
}

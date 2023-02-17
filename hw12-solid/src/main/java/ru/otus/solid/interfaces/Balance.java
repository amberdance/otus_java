package ru.otus.solid.interfaces;

public interface Balance {

    int remains();

    void deposit(int cash);

    void withdraw(int cash);
}

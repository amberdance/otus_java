package ru.otus.solid;

public interface Balance {

    int remains();

    void deposit(int cash);

    void withdraw(int cash);
}

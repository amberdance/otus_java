package ru.otus.solid.atm;

public enum Nominal {
    NOMINAL_100(100),
    NOMINAL_500(500),
    NOMINAL_1000(1000),
    NOMINAL_5000(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
package ru.otus.solid.atm;

public enum Nominal {
    N_50(50),
    N_100(100),
    N_200(200),
    N_500(500),
    N_1000(1000),
    N_2000(2000),
    N_5000(5000);

    private final int value;

    Nominal(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
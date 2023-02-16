package ru.otus.solid.interfaces;

public enum Nominal {
    NOMINAL_100(100),
    NOMINAL_500(500),
    NOMINAL_1000(1000),
    NOMINAL_5000(5000);

    private final int representation;

    Nominal(int representation) {
        this.representation = representation;
    }

    public int representation() {
        return representation;
    }

}
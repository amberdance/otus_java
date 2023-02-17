package ru.otus.solid.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Banknote {
    N_50(50), N_100(100), N_200(200), N_500(500), N_1000(1000), N_2000(2000), N_5000(5000);

    private final int value;

    Banknote(int value) {
        this.value = value;
    }

    public static List<Banknote> valuesReversed() {
        var banknotes = new ArrayList<>(Arrays.stream(values()).toList());
        Collections.reverse(banknotes);

        return banknotes;
    }

    public int value() {
        return value;
    }
}
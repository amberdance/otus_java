package ru.otus.solid;

import ru.otus.solid.atm.Nominal;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        var t = 600;
        Map<Nominal, Integer> test = new HashMap<>();
        test.put(Nominal.NOMINAL_100, 0);
        test.put(Nominal.NOMINAL_500, 0);
        test.put(Nominal.NOMINAL_1000, 0);
        test.put(Nominal.NOMINAL_5000, 0);

        do {
            System.out.println(t);

            if (t % 5000 == 0) {
                test.put(Nominal.NOMINAL_5000, test.get(Nominal.NOMINAL_5000) + 1);
                t -= 5000;
                continue;
            }

            if (t % 1000 == 0) {
                test.put(Nominal.NOMINAL_1000, test.get(Nominal.NOMINAL_1000) + 1);
                t -= 1000;
                continue;
            }
            if (t % 500 == 0) {
                test.put(Nominal.NOMINAL_500, test.get(Nominal.NOMINAL_500) + 1);
                t -= 500;
                continue;
            }

            if (t % 100 == 0) {
                test.put(Nominal.NOMINAL_100, test.get(Nominal.NOMINAL_100) + 1);
                t -= 100;
            }

        } while (t > 0);

        test = test.entrySet().stream().filter(entry -> entry.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(test);
    }
}

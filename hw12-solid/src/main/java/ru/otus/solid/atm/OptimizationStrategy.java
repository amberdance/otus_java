package ru.otus.solid.atm;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OptimizationStrategy {

    private Map<Nominal, Integer> slots;
    private int requestedCash;

    public OptimizationStrategy(Map<Nominal, Integer> slots, int requestedCash) {
        this.slots = slots;
        this.requestedCash = requestedCash;
    }

    public OptimizationStrategy optimizeByDivideNominals() {
        var nominalsReversed = Arrays.stream(Nominal.values()).sorted((a, b) -> b.value() - a.value()).toList();

        do {
            for (Nominal nominal : nominalsReversed) {
                var currentNominal = nominal.value();

                if (requestedCash % currentNominal == 0) {
                    slots.put(nominal, slots.get(nominal) + 1);
                    requestedCash -= currentNominal;
                    break;
                }
            }
        } while (requestedCash > 0);

        slots = slots.entrySet().stream().filter(entry -> entry.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return this;
    }

    public Map<Nominal, Integer> getResult() {
        return slots;
    }

}

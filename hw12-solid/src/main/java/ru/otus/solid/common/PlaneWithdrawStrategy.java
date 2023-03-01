package ru.otus.solid.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.OptimizationStrategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class PlaneWithdrawStrategy implements OptimizationStrategy<Banknote, Integer> {

    private final Map<Banknote, Integer> slots = new HashMap<>();
    private int requestedCash;

    public PlaneWithdrawStrategy(int requestedCash) {
        this.requestedCash = requestedCash;
    }

    @Override
    public OptimizationStrategy<Banknote, Integer> optimize() {
        var banknotes = new java.util.ArrayList<>(Arrays.stream(Banknote.values()).toList());
        Collections.reverse(banknotes);

        for (Banknote banknote : banknotes) {
            if (requestedCash == 0) break;

            var nominalCost = banknote.value();
            var remainByDivide = requestedCash % nominalCost;

            if (remainByDivide == 0) {
                slots.put(banknote, requestedCash / nominalCost);
            } else {
                if (requestedCash < nominalCost) continue;
                slots.put(banknote, (requestedCash - remainByDivide) / nominalCost);
            }

            requestedCash -= (requestedCash / nominalCost) * nominalCost;
        }

        return this;
    }

    @Override
    public Map<Banknote, Integer> getResult() {
        return slots;
    }

}

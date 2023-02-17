package ru.otus.solid.common;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.interfaces.OptimizationStrategy;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class DivisionByReminderStrategy implements OptimizationStrategy<Banknote, Integer> {

    private final Map<Banknote, Integer> slots = new HashMap<>();
    private int requestedCash;

    public DivisionByReminderStrategy(int requestedCash) {
        this.requestedCash = requestedCash;
    }

    /**
     * Решение не лучшее, хотелось бы применить разделяй и властвуй, но власти и опыта.
     * Принцип работы:
     * Сумма к снятию - 26000.
     * На каждой итерации по отсортированным в порядке убывания номиналам (важно) (5000, 2000, 1000 и тд) делается
     * проверка:
     * 1) Если число делится c остатком на текущий номинал, то:
     * - 26000 - 1000 (остаток) = 25000
     * - 25000 / 5000 (текущий номинал) = 5 (кол-во купюр)
     * - 26000 - 25000 = 1000 (вычитается остаток от запрошенной суммы и переход на следующую итерацию, с оставшейся
     * суммой (1000)
     * 2) Если число делится полностью без остатка, то
     * - 25000 % 5000 = 0
     * - 25000 / (текущий номинал) = 5 (кол-во купюр)
     * - 25000 - 25000 = 0 (остатков не осталось, выход из цикла)
     */
    @Override
    public OptimizationStrategy<Banknote, Integer> optimize() {
        for (Banknote banknote : Banknote.valuesReversed()) {
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

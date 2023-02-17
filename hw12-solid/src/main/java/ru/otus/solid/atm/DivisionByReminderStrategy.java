package ru.otus.solid.atm;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.otus.solid.interfaces.OptimizationStrategy;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
public class DivisionByReminderStrategy implements OptimizationStrategy<Nominal, Integer> {

    private final Map<Nominal, Integer> slots = new HashMap<>();
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
    public OptimizationStrategy<Nominal, Integer> optimize() {
        for (Nominal nominal : Nominal.valuesReversed()) {
            if (requestedCash == 0) break;

            var nominalCost = nominal.value();
            var remainByDivide = requestedCash % nominalCost;

            if (remainByDivide == 0) {
                slots.put(nominal, requestedCash / nominalCost);
            } else {
                if (requestedCash < nominalCost) continue;
                slots.put(nominal, (requestedCash - remainByDivide) / nominalCost);
            }

            requestedCash -= (requestedCash / nominalCost) * nominalCost;
        }

        return this;
    }

    @Override
    public Map<Nominal, Integer> getResult() {
        return slots;
    }

}

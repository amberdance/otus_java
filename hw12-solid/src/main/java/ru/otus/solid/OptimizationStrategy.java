package ru.otus.solid;

import java.util.Map;

public interface OptimizationStrategy<K, V> {

    OptimizationStrategy<K, V> optimize();

    Map<K, V> getResult();
}

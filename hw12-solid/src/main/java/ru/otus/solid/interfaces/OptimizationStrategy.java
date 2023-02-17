package ru.otus.solid.interfaces;

import java.util.Map;

public interface OptimizationStrategy<K, V> {

    OptimizationStrategy<K, V> optimize();

    Map<K, V> getResult();
}

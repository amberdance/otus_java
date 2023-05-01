package ru.otus.test;

public interface Resultable<K, V> {
    void setValue(K key, V value);

    void incrementValue(K key);

    void print();

}

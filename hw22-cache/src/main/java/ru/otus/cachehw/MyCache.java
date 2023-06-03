package ru.otus.cachehw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    private final WeakHashMap<K, V> weakHashMap = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        weakHashMap.put(key, value);
        notifyAll(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = weakHashMap.remove(key);
        notifyAll(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = weakHashMap.get(key);
        notifyAll(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAll(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.error("Exception in listener", e);
            }
        });
    }
}

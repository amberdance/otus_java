package ru.otus.cache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HWCacheDemo {


    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();
        HwListener<String, Integer> listener =
                (key, value, action) -> log.info("key:{}, value:{}, action: {}",
                        key, value, action);

        cache.addListener(listener);
        cache.put("1", 1);

        log.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        cache.removeListener(listener);
    }
}

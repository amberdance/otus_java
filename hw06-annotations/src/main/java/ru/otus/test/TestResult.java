package ru.otus.test;

import java.util.HashMap;
import java.util.Map;

public class TestResult implements Resultable<String, Integer> {
    private final Map<String, Integer> results = new HashMap<>() {{
        put(Values.STATS_FAILED_KEY.getValue(), 0);
        put(Values.STATS_PASSED_KEY.getValue(), 0);
        put(Values.STATS_TEST_KEY.getValue(), 0);
    }};

    @Override
    public void incrementValue(String key) {
        results.put(key, results.get(key) + 1);
    }

    @Override
    public void setValue(String key, Integer value) {
        results.put(key, value);
    }


    @Override
    public void print() {
        System.out.printf("Total tests: %d ,Passed: %d, Failed: %d\n",
                results.get(Values.STATS_TEST_KEY.getValue()),
                results.get(Values.STATS_PASSED_KEY.getValue()),
                results.get(Values.STATS_FAILED_KEY.getValue()));

        System.out.println("-".repeat(100));
    }
}

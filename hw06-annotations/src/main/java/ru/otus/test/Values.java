package ru.otus.test;

public enum Values {
    STATS_PASSED_KEY("PASSED"),
    STATS_TEST_KEY("TESTS"),
    STATS_FAILED_KEY("FAILED");


    private final String val;

    Values(String val) {
        this.val = val;
    }

    public String getValue() {
        return val;
    }
}

package ru.otus.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestLogger {
    private static final List<String> logger = new ArrayList<>();

    private TestLogger() {
    }

    public static void printLogs() {
        logger.forEach(System.out::println);
    }

    public static void log(String value) {
        Objects.requireNonNull(value);
        logger.add(value);
    }

    public static void log(String value, int index) {
        Objects.requireNonNull(value);
        logger.add(index, value);
    }

    public static void clearLog() {
        logger.clear();
    }

    public enum LogFormat {
        STATISTICS_FORMAT("Total:%d Passed:%d Failed:%d"),
        PASSED_TEST_FORMAT("%s: passed"),

        FAILED_TEST_FORMAT("%s: failed - %s");
        private final String value;

        LogFormat(String s) {
            value = s;
        }

        public String value() {
            return value;
        }
    }
}

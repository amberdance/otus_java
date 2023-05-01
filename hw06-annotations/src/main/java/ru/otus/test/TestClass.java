package ru.otus.test;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

public class TestClass {
    private static final StringBuilder SOME_INSTANCE = new StringBuilder();

    @Before
    void setUp() {
        SOME_INSTANCE.append(Math.round(Math.random() * 100));
    }

    @Test
    void test1() {
        throw new RuntimeException("Test failed");
    }

    @Test
    void test2() {
    }

    @Test
    void test3() {
    }


    @After
    void tearDown() {
        SOME_INSTANCE.setLength(0);
    }
}
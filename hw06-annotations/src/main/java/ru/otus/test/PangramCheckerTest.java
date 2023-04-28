package ru.otus.test;

import ru.otus.PangramChecker;
import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import static ru.otus.assertion.Assertion.assertTrue;

public class PangramCheckerTest {
    private static final StringBuilder SOME_INSTANCE = new StringBuilder();

    @Before
    void setUp() {
        SOME_INSTANCE.append(Math.round(Math.random() * 100));
    }

    @Test
    void test1() {
        assertTrue(true, PangramChecker.isPangram("The quick brown fox jumps over the lazy dog"));
    }

    @Test
    void test2() {
        assertTrue(false, PangramChecker.isPangram("You shall not pass!"));
    }

    @Test
    void test3() {
        assertTrue(true, PangramChecker.isPangram("ASxzc123g,m,m,m,motiu435h13kj12hkj3kjh12jk3"));
    }


    @After
    void tearDown() {
        SOME_INSTANCE.setLength(0);
    }
}
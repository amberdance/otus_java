package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import static ru.otus.Assertion.assertTrue;

public class PangramCheckerTest {
    private static final StringBuilder SOME_INSTANCE = new StringBuilder();

    @Before
    void setUp() {
        SOME_INSTANCE.append(Math.round(Math.random() * 100));
        System.out.println("StringBuilder content before -> " + SOME_INSTANCE);
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
        System.out.println("StringBuilder content after -> " + SOME_INSTANCE);
    }
}
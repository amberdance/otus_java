package ru.otus;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.otus.assertions.Assertion.assertEquals;
import static ru.otus.assertions.Assertion.assertTrue;

public class PangramCheckerTest {
    private static final String FILE_NAME = "test";
    private static OutputStream outputStream;


    @Before
    void setUp() throws IOException {
        outputStream = Files.newOutputStream(Path.of(FILE_NAME));
        outputStream.write("something".getBytes(Charset.defaultCharset()));
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
        assertTrue(true, PangramChecker.isPangram(PangramChecker.ALPHABET_ENG));
    }

    @Test
    void test4() {
        assertTrue(true, PangramChecker.isPangram("ASxzc123g,m,m,m,motiu435h13kj12hkj3kjh12jk3"));
    }

    @Test
    void test5() {
        assertEquals("ABC", "abc");
    }

    @After
    void tearDown() {
        try {
            outputStream.close();
            outputStream.write("\n something else".getBytes());
        } catch (IOException e) {
//            System.out.println("Cannot write to closed OutputStream");
        } finally {
            try {
                Files.delete(Path.of(FILE_NAME));
            } catch (IOException ignored) {
            }
        }
    }
}
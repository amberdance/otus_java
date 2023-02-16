package ru.otus.solid.atm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ATMMetaTest {


    @Test
    void testBuilder() {
        var meta =
                new ATMMeta.ATMMetaBuilder().corporation("testcorp").contactCenter("8-800-444").version("66.66").hardwareId("qwertyisnotgood").build();

        assertEquals("testcorp", meta.getCorporation());
        assertEquals("8-800-444", meta.getContactCenter());
        assertEquals("66.66", meta.getVersion());
        assertEquals("qwertyisnotgood", meta.getHardwareId());
    }
}
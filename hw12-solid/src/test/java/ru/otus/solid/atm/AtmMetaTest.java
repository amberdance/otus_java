package ru.otus.solid.atm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtmMetaTest {


    @Test
    void testBuilder() {
        var meta = new AtmMeta.AtmMetaBuilder().corporation("TEST_CORPORATION").contactCenter("8-800-444").version(
                "66" + ".66").hardwareId("HARDWARE_ID").build();

        assertEquals("TEST_CORPORATION", meta.getCorporation());
        assertEquals("8-800-444", meta.getContactCenter());
        assertEquals("66.66", meta.getVersion());
        assertEquals("HARDWARE_ID", meta.getHardwareId());
    }
}
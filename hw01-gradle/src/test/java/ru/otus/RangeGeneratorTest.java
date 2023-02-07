package ru.otus;

import com.google.common.collect.RangeSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RangeGeneratorTest {

    @Test
    void itShouldReturnRangeSet() {
        assertInstanceOf(RangeSet.class, RangeGenerator.range());
        assertInstanceOf(RangeSet.class, RangeGenerator.randomRange());
    }

    @Test
    void itShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> RangeGenerator.range(90, 1));
    }
}

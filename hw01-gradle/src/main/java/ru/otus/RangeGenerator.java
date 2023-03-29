package ru.otus;

import java.util.Random;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

public class RangeGenerator {

    public static final int INITIAL_LOWER_BOUND = 0;
    public static final int INITIAL_UPPER_BOUND = 9999;

    private RangeGenerator() {
    }

    public static RangeSet<Integer> range() {
        return new ImmutableRangeSet.Builder<Integer>().add(Range.closed(INITIAL_LOWER_BOUND, INITIAL_UPPER_BOUND))
                .build();
    }

    public static RangeSet<Integer> range(int lower, int upper) {
        return new ImmutableRangeSet.Builder<Integer>().add(Range.closed(lower, upper)).build();
    }

    public static RangeSet<Integer> randomRange() {
        int upper = Math.abs(new Random().nextInt());
        int lower = Math.abs(new Random().nextInt(upper));

        return new ImmutableRangeSet.Builder<Integer>().add(Range.closed(lower, upper)).build();
    }
}

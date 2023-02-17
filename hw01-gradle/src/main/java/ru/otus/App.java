package ru.otus;

import static ru.otus.RangeGenerator.randomRange;
import static ru.otus.RangeGenerator.range;

import java.util.Random;

import com.google.common.collect.RangeSet;

public class App {
    private static RangeSet<Integer> defaultRangeSet = range();
    private static RangeSet<Integer> randomRangeSet = randomRange();

    private static final int CUSTOM_LOWER_BOUND = 20;
    private static final int CUSTOM_UPPER_BOUND = 66;
    private static RangeSet<Integer> customRangeSet = range(CUSTOM_LOWER_BOUND, CUSTOM_UPPER_BOUND);

    private static Integer randomInteger = Math.abs(new Random().nextInt());
    private static String format = "Is %d in range of %s - %b";

    public static void main(String[] args) {
        printRangeEntry();
    }

    private static void printRangeEntry() {
        System.out.println(String.format(format, RangeGenerator.INITIAL_UPPER_BOUND,
                defaultRangeSet, defaultRangeSet.contains(RangeGenerator.INITIAL_UPPER_BOUND)));

        System.out.println(
                String.format(format, CUSTOM_LOWER_BOUND, customRangeSet, customRangeSet.contains(CUSTOM_LOWER_BOUND)));

        System.out.println(String.format(format, randomInteger, randomRangeSet,
                randomRangeSet.contains(randomInteger)));

    }
}

package ru.otus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A pangram is a sentence that contains every single letter of the alphabet at least once. For example, the sentence
 * "The quick brown fox jumps over the lazy dog" is a pangram, because it uses the letters A-Z at least once (case is
 * irrelevant).
 *
 * @see
 * <a href="https://www.codewars.com/kata/545cedaa9943f7fe7b000048/train/java"> https://www.codewars.com/kata/545cedaa9943f7fe7b000048/train/java</a>
 */

public class PangramChecker {
    public static final String ALPHABET_ENG = "abcdefghijklmnopqrstuvwxyz";

    public static boolean isPangram(String sentence) {
        List<String> alphabetList = new ArrayList<>(Arrays.stream(ALPHABET_ENG.split("")).toList());
        List<String> sentenceLettersList =
                Arrays.stream(sentence.toLowerCase().replaceAll("\\W", "").split("")).toList();

        alphabetList.removeAll(sentenceLettersList);

        return alphabetList.isEmpty();
    }
}
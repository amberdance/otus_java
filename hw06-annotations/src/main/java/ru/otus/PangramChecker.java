package ru.otus;

public class PangramChecker {

    public static boolean isPangram(String s) {
        var lettersExisting = new boolean[26];

        for (var c : s.toCharArray()) {
            var letterIndex = c - (Character.isUpperCase(c) ? 'A' : 'a');

            if (letterIndex >= 0 && letterIndex < lettersExisting.length) {
                lettersExisting[letterIndex] = true;
            }
        }

        for (var letterFlag : lettersExisting) {
            if (!letterFlag) {
                return false;
            }
        }

        return true;
    }
}
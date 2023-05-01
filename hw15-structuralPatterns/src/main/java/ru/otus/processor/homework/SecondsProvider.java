package ru.otus.processor.homework;

@FunctionalInterface
public interface SecondsProvider {
    int getCurrentSecondOfMinute();
}
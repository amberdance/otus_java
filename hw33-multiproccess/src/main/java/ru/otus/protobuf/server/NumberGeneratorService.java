package ru.otus.protobuf.server;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NumberGeneratorService {

    public void generate(int from, int to, Consumer<Integer> consumer) {
        for (int i = from; i <= to; i++) {
            consumer.accept(i);

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

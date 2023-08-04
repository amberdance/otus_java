package ru.otus.protobuf.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.protobuf.generated.NumberGeneratorGrpc;
import ru.otus.protobuf.generated.RunMessage;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class NumberProcessor {

    private final NumberGeneratorGrpc.NumberGeneratorStub stub;
    private final int from;
    private final int to;
    private int received;


    public void start(int from, int to) {
        stub.generate(RunMessage.newBuilder().setFrom(from).setTo(to).build(), new NumberMessageStreamObserver(this));

        process();
    }

    public synchronized void setReceived(int received) {
        this.received = received;
    }

    private void process() {
        int currentValue = 0;

        for (int i = from; i <= to; i++) {
            synchronized (this) {
                currentValue += received + 1;
            }

            log.info("Iteration: {}, Received: {}, value: {}", i, received, currentValue);

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

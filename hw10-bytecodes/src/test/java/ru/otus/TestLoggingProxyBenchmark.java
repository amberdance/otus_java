package ru.otus;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
public class TestLoggingProxyBenchmark {

    private static final int ITERATIONS = 10000;

    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void benchmarkWithCollectMethodsInConstructor() {
        iterate(new TestLoggingProxy().getProxy());
    }

    private void iterate(TestLoggingInterface log) {
        for (int i = 0; i < ITERATIONS; i++) {
            log.calculation(1);
            log.calculation(1, 2);
            log.calculation(1, 2, 3);
            log.calculation(1, 2, 3, 4);
        }
    }

    @Benchmark
    public void benchmarkWithCheckMethodInsideInvoke() {
        iterate(new TestLoggingProxyOld().getProxy());
    }
}
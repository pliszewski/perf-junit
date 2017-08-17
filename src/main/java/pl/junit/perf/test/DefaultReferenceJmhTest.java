package pl.junit.perf.test;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import pl.junit.perf.PerformanceTestFrame;
import pl.junit.perf.annotations.ReferenceTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pliszewski on 14.08.2017.
 */
public class DefaultReferenceJmhTest extends PerformanceTestFrame {

    private List<Integer> integers;

    private Blackhole blackhole;

    @Setup(Level.Trial)
    public void setUp(Blackhole bh) {
        blackhole = bh;
        integers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 17_000; i++) {
            integers.add(rand.nextInt());
        }
    }

    @Test
    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    @Fork(value = 1)
    @ReferenceTest
    public void test() {
        blackhole.consume(BubbleSort.sort(integers));
    }


}

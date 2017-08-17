package pl.junit.perf.samples;

import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import pl.junit.perf.PerformanceTestFrame;
import pl.junit.perf.annotations.PerformanceTest;
import pl.junit.perf.test.BubbleSort;
import pl.junit.perf.test.DefaultReferenceJmhTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pliszewski on 14.08.2017.
 */
public class BubbleSortJmhTest extends PerformanceTestFrame {

    private List<Integer> integers;

    private Blackhole blackhole;

    @Setup(Level.Trial)
    public void setUp(Blackhole bh) {
        blackhole = bh;
        integers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 11_000; i++) {
            integers.add(rand.nextInt());
        }
    }

    @Test
    @Benchmark
    @Warmup(iterations = 10)
    @Measurement(iterations = 10)
    @Fork(1)
    @PerformanceTest(referenceTest = DefaultReferenceJmhTest.class, executionTime = 0.36, deviationThreshold = 5)
    public void test() {
        blackhole.consume(BubbleSort.sort(integers));
    }
}

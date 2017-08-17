package pl.junit.perf.samples;

import org.junit.Test;
import pl.junit.perf.PerformanceTestFrame;
import pl.junit.perf.annotations.PerformanceTest;
import pl.junit.perf.test.BubbleSort;
import pl.junit.perf.test.DefaultReferenceTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 14.08.2017.
 */
public class BubbleSortTest extends PerformanceTestFrame {

    private static final List<Integer> integers;

    static {
        integers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 11_000; i++) {
            integers.add(rand.nextInt());
        }
    }

    @Test
    @PerformanceTest(referenceTest = DefaultReferenceTest.class,
            executionTime = 0.37,
            warmupIterations = 10,
            iterations = 10,
            timeUnit = TimeUnit.MILLISECONDS)
    public void test() {
        BubbleSort.sort(integers);
    }
}

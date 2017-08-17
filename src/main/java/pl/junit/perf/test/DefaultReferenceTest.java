package pl.junit.perf.test;

import org.junit.Test;
import pl.junit.perf.PerformanceTestFrame;
import pl.junit.perf.annotations.ReferenceTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pliszewski on 14.08.2017.
 */
public class DefaultReferenceTest extends PerformanceTestFrame {

    private static final List<Integer> integers;

    static {
        integers = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 17_000; i++) {
            integers.add(rand.nextInt());
        }
    }

    @Test
    @ReferenceTest(warmupIterations = 10, iterations = 10)
    public void test() {
        BubbleSort.sort(integers);
    }

}

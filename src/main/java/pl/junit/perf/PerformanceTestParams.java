package pl.junit.perf;

import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 14.08.2017.
 */
public class PerformanceTestParams {

    private int iterations;

    private int warmupIterations;

    private TimeUnit timeUnit;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public void setWarmupIterations(int warmupIterations) {
        this.warmupIterations = warmupIterations;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}

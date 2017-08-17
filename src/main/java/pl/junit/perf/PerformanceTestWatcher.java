package pl.junit.perf;

import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.Statement;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import pl.junit.perf.annotations.PerformanceTest;
import pl.junit.perf.annotations.ReferenceTest;
import pl.junit.perf.utils.LogMessageBuilder;
import pl.junit.perf.utils.PerfTestUtil;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 17.07.2017.
 */
public class PerformanceTestWatcher implements TestRule {

    /**
     * Static, thread safe map of reference tests and their's execution times
     * Nanosecond is a time unit.
     */
    private static ConcurrentHashMap<String, Optional<Long>> referenceTestsTimes = new ConcurrentHashMap<>();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            public void evaluate() throws Throwable {
                PerformanceTestParams params = prepareParamsAndExecuteRefTestIfNeeded(description);
                starting(description);
                warmup(base, params);
                long executionTime = executeTestWIthTimeMeasurement(base, description, params);
                finished(description, executionTime, params);
                validatePerformance(description, executionTime, params);
            }
        };
    }

    private PerformanceTestParams prepareParamsAndExecuteRefTestIfNeeded(Description description) {
        PerformanceTestParams performanceTestParams = new PerformanceTestParams();
        ReferenceTest referenceTest = getReferenceTestAnnotation(description);
        if (referenceTest != null) {
            performanceTestParams.setIterations(referenceTest.iterations());
            performanceTestParams.setWarmupIterations(referenceTest.warmupIterations());
            performanceTestParams.setTimeUnit(TimeUnit.NANOSECONDS);
        } else {
            PerformanceTest performanceTest = getPerformanceTestAnnotation(description);
            performanceTestParams.setIterations(performanceTest.iterations());
            performanceTestParams.setWarmupIterations(performanceTest.warmupIterations());
            performanceTestParams.setTimeUnit(performanceTest.timeUnit());
            executeReferenceTestIfNecessary(performanceTest);
        }
        return performanceTestParams;
    }

    private void executeReferenceTestIfNecessary(PerformanceTest performanceTest) {
        Class refTestClass = performanceTest.referenceTest();
        Optional<Long> refTestTimeValue = referenceTestsTimes.get(refTestClass.getName());
        if (refTestTimeValue == null) {
            referenceTestsTimes.put(refTestClass.getName(), Optional.empty());
            executeReferenceTest(refTestClass);
        } else if (!refTestTimeValue.isPresent()) {
            executeReferenceTest(refTestClass);
        }
    }

    private static void starting(Description description) {
        System.out.println("\n[PERFORMANCE TEST]\nTest " + PerfTestUtil.getTestId(description) + " started");
    }

    private void warmup(Statement base, PerformanceTestParams params) throws Throwable {
        for (int i = 0; i < params.getWarmupIterations(); i++) {
            long startTime = System.nanoTime();
            base.evaluate();
            System.out.println("Warmup iteration " + (i + 1) + ": " + PerfTestUtil.formatExecutionTime(System.nanoTime() - startTime, params.getTimeUnit()));
        }
    }

    private long executeTestWIthTimeMeasurement(Statement base, Description description, PerformanceTestParams params) throws Throwable {
        if (isJmhTest(description)) {
            return executeJmhTest(description);
        }
        return executeJunitTest(base, params);
    }

    private boolean isJmhTest(Description description) {
        return description.getAnnotation(Benchmark.class) != null;
    }

    private long executeJunitTest(Statement base, PerformanceTestParams params) throws Throwable {
        long executionTimeSum = 0;
        for (int i = 0; i < params.getIterations(); i++) {
            long startTime = System.nanoTime();
            base.evaluate();
            long execTime = System.nanoTime() - startTime;
            executionTimeSum += execTime;
            System.out.println("Execution iteration " + (i + 1) + ": " + PerfTestUtil.formatExecutionTime(execTime, params.getTimeUnit()));
        }
        return executionTimeSum / params.getIterations();
    }

    private static long executeJmhTest(Description description) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include("^" + PerfTestUtil.getTestId(description) + "$")
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.NANOSECONDS)
                .build();
        Collection<RunResult> run = new Runner(opt).run();
        return (long) run.iterator().next().getPrimaryResult().getScore();
    }

    private void validatePerformance(Description description, long executionTime, PerformanceTestParams params) {
        String testClassName = description.getClassName();
        String testId = PerfTestUtil.getTestId(description);
        ReferenceTest referenceTest = getReferenceTestAnnotation(description);
        if (referenceTest != null) {
            referenceTestsTimes.put(testClassName, Optional.of(executionTime));
            String refTestMessage = "Reference test: " + testId + " avg execution time: " + PerfTestUtil.formatExecutionTime(executionTime, params.getTimeUnit()) + " saved";
            System.out.println(refTestMessage);
            return;
        }

        PerformanceTest performanceTest = getPerformanceTestAnnotation(description);

        Class refTestClass = performanceTest.referenceTest();
        Long refTestTime = referenceTestsTimes.get(refTestClass.getName()).orElse(0L);

        double executionTimeInReferenceTestUnit;
        if (refTestTime > 0) {
            executionTimeInReferenceTestUnit = (double) executionTime / refTestTime;
        } else {
            throw new RuntimeException("Reference test execution time is equal zero");
        }

        double upperLimit = performanceTest.executionTime() * (100 + performanceTest.deviationThreshold()) / 100;

        String testLogMessage = new LogMessageBuilder()
                .setDescription(description)
                .setExecutionTime(executionTime)
                .setPerformanceTest(performanceTest)
                .setRefTestTime(refTestTime)
                .setExecutionTimeInReferenceTestUnit(executionTimeInReferenceTestUnit)
                .setUpperLimit(upperLimit)
                .setParams(params)
                .createLogMessage();

        if (executionTimeInReferenceTestUnit > upperLimit) {
            Assert.fail(testLogMessage);
        } else {
            System.out.println(testLogMessage);
        }
    }

    private void finished(Description description, long executionTime, PerformanceTestParams params) {
        System.out.println("Test " + PerfTestUtil.getTestId(description) + " finished, avg time: " + PerfTestUtil.formatExecutionTime(executionTime, params.getTimeUnit()));
    }

    private void executeReferenceTest(Class<?> testClass) {
        JUnitCore.runClasses(testClass);
    }

    private PerformanceTest getPerformanceTestAnnotation(Description description) {
        PerformanceTest performanceTest = description.getAnnotation(PerformanceTest.class);
        if (performanceTest == null) {
            performanceTest = description.getTestClass().getAnnotation(PerformanceTest.class);
        }
        if (performanceTest == null && getReferenceTestAnnotation(description) == null) {
            throw new IllegalStateException("PerformanceTest or ReferenceTest annotation is missing for " + PerfTestUtil.getTestId(description) + ". Add annotation for test method or class.");
        }
        return performanceTest;
    }

    private ReferenceTest getReferenceTestAnnotation(Description description) {
        return description.getAnnotation(ReferenceTest.class);
    }
}
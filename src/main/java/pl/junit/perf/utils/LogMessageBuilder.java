package pl.junit.perf.utils;

import org.junit.runner.Description;
import pl.junit.perf.PerformanceTestParams;
import pl.junit.perf.annotations.PerformanceTest;

public class LogMessageBuilder {

    private Description description;

    private long executionTime;

    private PerformanceTest performanceTest;

    private Long refTestTime;

    private double executionTimeInReferenceTestUnit;

    private double upperLimit;

    private PerformanceTestParams params;

    public LogMessageBuilder setDescription(Description description) {
        this.description = description;
        return this;
    }

    public LogMessageBuilder setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public LogMessageBuilder setPerformanceTest(PerformanceTest performanceTest) {
        this.performanceTest = performanceTest;
        return this;
    }

    public LogMessageBuilder setRefTestTime(Long refTestTime) {
        this.refTestTime = refTestTime;
        return this;
    }

    public LogMessageBuilder setExecutionTimeInReferenceTestUnit(double executionTimeInReferenceTestUnit) {
        this.executionTimeInReferenceTestUnit = executionTimeInReferenceTestUnit;
        return this;
    }

    public LogMessageBuilder setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
        return this;
    }

    public LogMessageBuilder setParams(PerformanceTestParams params) {
        this.params = params;
        return this;
    }

    public String createLogMessage() {
        double deviation = 100 * (executionTimeInReferenceTestUnit / performanceTest.executionTime() - 1);
        return "\nSUMMARY for " + PerfTestUtil.getTestId(description) + ":" +
                "\nReference test avg execution time: " + PerfTestUtil.formatExecutionTime(refTestTime, params.getTimeUnit()) +
                "\nExpected average execution times [reference unit]: " + performanceTest.executionTime() +
                "\nDeviation threshold: " + performanceTest.deviationThreshold() + "%" +
                "\nUpper limit [reference unit]: " + upperLimit +
                "\nAvg execution time: " + PerfTestUtil.formatExecutionTime(executionTime, params.getTimeUnit()) +
                "\nAvg execution time [reference unit]: " + executionTimeInReferenceTestUnit +
                "\nDeviation: " + PerfTestUtil.formatPercentage(deviation);
    }
}
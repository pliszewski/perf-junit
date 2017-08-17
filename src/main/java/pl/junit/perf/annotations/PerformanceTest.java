package pl.junit.perf.annotations;

import pl.junit.perf.test.DefaultReferenceTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 17.07.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface PerformanceTest {

    /**
     * @return class of reference test - should contain only one test method
     */
    Class referenceTest() default DefaultReferenceTest.class;

    /**
     * @return expected execution time in reference test time unit
     */
    double executionTime();

    /**
     * @return percentage deviation from average execution time
     * default 10%
     */
    int deviationThreshold() default 5;

    /**
     * @return number of test method executions
     */
    int iterations() default 1;

    /**
     * @return number of test method executions not included in average time calculations - only for warmup JVM purpose
     */
    int warmupIterations() default 0;

    /**
     * @return time unit printed in logs
     */
    TimeUnit timeUnit() default TimeUnit.NANOSECONDS;
}
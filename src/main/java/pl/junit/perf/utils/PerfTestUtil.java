package pl.junit.perf.utils;

import org.junit.runner.Description;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 15.08.2017.
 */
public class PerfTestUtil {


    public static String getTestId(Description description) {
        return description.getClassName() + "." + description.getMethodName();
    }

    public static String formatExecutionTime(long executionTime, TimeUnit timeUnit) {
        return String.format("%,d %s", timeUnit.convert(executionTime, TimeUnit.NANOSECONDS), timeUnit.name().toLowerCase());
    }

    public static String formatPercentage(double percentage) {
        return String.format(Locale.US, "%.2f", percentage) + "%";
    }
}

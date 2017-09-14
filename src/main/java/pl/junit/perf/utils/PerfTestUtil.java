package pl.junit.perf.utils;

import org.junit.runner.Description;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by pliszewski on 15.08.2017.
 */
public class PerfTestUtil {

    private static final DecimalFormat NUMBER_FORMAT = (DecimalFormat) DecimalFormat.getInstance(Locale.US);

    private static final char DECIMAL_SEPARATOR = '.';

    private static final char GROUPING_SEPARATOR = ' ';

    static {
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator(DECIMAL_SEPARATOR);
        customSymbol.setGroupingSeparator(GROUPING_SEPARATOR);
        NUMBER_FORMAT.setDecimalFormatSymbols(customSymbol);
        NUMBER_FORMAT.setGroupingUsed(true);
    }

    public static String getTestId(Description description) {
        return description.getClassName() + "." + description.getMethodName();
    }

    public static String formatExecutionTime(long executionTime, TimeUnit timeUnit) {
        return NUMBER_FORMAT.format(timeUnit.convert(executionTime, TimeUnit.NANOSECONDS)) + " " + timeUnit.name().toLowerCase();
    }

    public static String formatPercentage(double percentage) {
        return String.format(Locale.US, "%.2f", percentage) + "%";
    }
}

package pl.junit.perf.utils;

import org.junit.Test;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by pliszewski on 17.08.2017.
 */
public class PerfTestUtilTest {

    @Test
    public void getTestIdTest() {
        Description description = Description.createTestDescription(PerfTestUtilTest.class, "testMethod");
        assertThat(PerfTestUtil.getTestId(description), is("pl.junit.perf.utils.PerfTestUtilTest.testMethod"));
    }

    @Test
    public void formatExecutionTimeTest() {
        assertThat(PerfTestUtil.formatExecutionTime(11_000_000_000L, TimeUnit.SECONDS), is("11 seconds"));
    }

    @Test
    public void formatPercentageTest() {
        assertThat(PerfTestUtil.formatPercentage(1.125), is("1.13%"));
    }
}

package pl.junit.perf;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import pl.junit.perf.annotations.PerformanceTest;

/**
 * Created by pliszewski on 16.08.2017.
 */
public class FailPerformanceTest {

    private static ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUp() {
        expectedException.expect(AssertionError.class);
    }

    @Rule
    public TestRule chain = RuleChain.outerRule(expectedException)
            .around(new PerformanceTestWatcher());

    @Test
    @PerformanceTest(executionTime = 0.1)
    public void test() throws InterruptedException {
        Thread.sleep(1000);
    }
}

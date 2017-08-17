package pl.junit.perf;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 * Created by pliszewski on 16.08.2017.
 */
public class MissingAnnotationTest {

    private static ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setUp() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("PerformanceTest or ReferenceTest annotation is missing");
    }

    @Rule
    public TestRule chain = RuleChain.outerRule(expectedException)
            .around(new PerformanceTestWatcher());


    @Test(expected = IllegalStateException.class)
    public void test(){
    }
}

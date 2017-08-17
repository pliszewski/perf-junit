package pl.junit.perf;

import org.junit.Rule;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * Created by pliszewski on 20.07.2017.
 */
@State(Scope.Benchmark)
public abstract class PerformanceTestFrame {

    @Rule
    public PerformanceTestWatcher performanceTestWatcher = new PerformanceTestWatcher();
}

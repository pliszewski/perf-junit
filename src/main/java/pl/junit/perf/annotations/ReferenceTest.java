package pl.junit.perf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pliszewski on 17.07.2017.
 * In referenced test class there can be only one method with this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ReferenceTest {

    /**
     * @return number of test method executions
     */
    int iterations() default 1;

    /**
     * @return number of test method executions not included in average time calculations - only for warmup JVM purpose
     */
    int warmupIterations() default 0;
}
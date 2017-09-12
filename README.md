# Perf-jUnit
Performance jUnit test framework

[![Build Status](https://travis-ci.org/pliszewski/perf-junit.svg?branch=master)](https://travis-ci.org/pliszewski/perf-junit)

## Description
```Perf-jUnit``` was created in order to automatization of performance monitoring of your code. The main idea is to run test, measure the time and compare to time of reference test. **Thanks to that, you don't have to worry about running the test on different machines**. Using ```Perf-jUnit```, you decrare expected time in comparison to reference test and define acceptable threshold of deviation of this time (default value is 5%).  

**```Perf-jUnit``` works with ```jUnit``` tests**. Adding special annotation ```@PerformanceTest``` you can define:
- number of warmup iterations (to warm up JVM),
- number of iterations(executions) included to calcuations of average execution time,
- reference test class
- expected execution time in reference test time unit
- acceptable percentage deviation from average execution time (default is 5%)
- time unit printed in logs (default is miliseconds)

What more **Perf-jUnit works with [```JMH```](http://openjdk.java.net/projects/code-tools/jmh/) tests**, so you can use JMH annotations to define performance test.
## Quickstart


1) include ```Perf-jUnit``` in your project ```pom.xml```
    ```xml
    <repositories>
    	<repository>
    	    <id>jitpack.io</id>
    	    <url>https://jitpack.io</url>
    	</repository>
    </repositories>
    ```
    ```xml
    <dependency>
        <groupId>com.github.pliszewski</groupId>
        <artifactId>perf-junit</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```

	
2) Prepare reference test or use one of defaults:
	* default tests:
		- ```DefaultReferenceTest.class``` - jUnit test of simple bubble sort implementation
        	```java
        	@Test
    		@ReferenceTest(warmupIterations = 10, iterations = 10)
    		public void test() {
    			BubbleSort.sort(integers);
    		}
        	```
		- ```DefaultReferenceJmhTest.class``` - JMH test of simple bubble sort implementation
        	```java
            @Test
			@Benchmark
			@Warmup(iterations = 10)
			@Measurement(iterations = 10)
			@Fork(value = 1)
			@ReferenceTest
			public void test() {
				blackhole.consume(BubbleSort.sort(integers));
			}
			```
	* or you can define your reference test:
		- create class with one test (simple ```jUnit``` or ```JMH``` (also requires ```@Test``` annotation)) based on defaults
		- add annotation ```@ReferenceTest```

3) create your performance test
	* create ```jUnit``` or ```JMH``` test (JMH test also requires ```@Test``` annotation)
	* extend class with ```PerformanceTestFrame``` class or add ```PerformanceTestWatcher``` rule to your test 
    	```java
        @Rule
        public PerformanceTestWatcher performanceTestWatcher = new PerformanceTestWatcher();
        ```
	* add annotation ```@PerformanceTest``` and define proper attributes of this annotation
		- before first execution attribute ```executionTime``` is hard to define so set whatever you want at this moment
		- ```jUnit``` test example
    		```java
    		@Test
    		@PerformanceTest(referenceTest = DefaultReferenceTest.class,
    				executionTime = 0.37,
    				warmupIterations = 10,
    				iterations = 10,
    				timeUnit = TimeUnit.MILLISECONDS)
    		public void test() {
    			BubbleSort.sort(integers);
    		}
            ```
		- ```JMH``` test example
            ```java			
            @Test
            @Benchmark
            @Warmup(iterations = 10)
            @Measurement(iterations = 10)
            @Fork(1)
            @PerformanceTest(referenceTest = DefaultReferenceJmhTest.class, executionTime = 0.36)
            public void test() {
            	blackhole.consume(BubbleSort.sort(integers));
            }
            ```	
4) run your test as ```jUnit``` test and analyze logs 
	- example of summary log
	    ```
		SUMMARY for pl.junit.perf.samples.BubbleSortTest.test:
		Reference test avg execution time: 1 053 milliseconds
		Expected average execution times [reference unit]: 0.37
		Deviation threshold: 5%
		Upper limit [reference unit]: 0.3885
		Avg execution time: 387 milliseconds
		Avg execution time [reference unit]: 0.36797196811935173
		Deviation: 1,55%
		```
	- the most important is ```Avg execution time``` expressed in reference test time units which should be set as ```expectedTime``` in ```@PerformanceTest``` annotation	


	
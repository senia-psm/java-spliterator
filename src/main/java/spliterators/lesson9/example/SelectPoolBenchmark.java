package spliterators.lesson9.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import spliterators.lesson7.example.ArrayExample;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class SelectPoolBenchmark {

    @Param({"100000"})
    public int length;

    public int[] array;

    @Setup
    public void setup() {
        array = new int[length];

        for (int i = 0; i < array.length; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
        }
    }


    @Benchmark
    public long sum1() throws ExecutionException, InterruptedException {

        ForkJoinPool p = new ForkJoinPool(1);
        return p.submit(() ->
                Arrays.stream(array)
                        .parallel()
                        .unordered()
                        .map(i -> {
                            Blackhole.consumeCPU(100);
                            return i;
                        })
                        .asLongStream()
                        .sum())
                .get();
    }

    @Benchmark
    public long sum2() throws ExecutionException, InterruptedException {

        ForkJoinPool p = new ForkJoinPool(2);
        return p.submit(() ->
                Arrays.stream(array)
                        .parallel()
                        .unordered()
                        .map(i -> {
                            Blackhole.consumeCPU(100);
                            return i;
                        })
                        .asLongStream()
                        .sum())
                .get();
    }

    @Benchmark
    public long sum4() throws ExecutionException, InterruptedException {

        ForkJoinPool p = new ForkJoinPool(4);
        return p.submit(() ->
                Arrays.stream(array)
                        .parallel()
                        .unordered()
                        .map(i -> {
                            Blackhole.consumeCPU(100);
                            return i;
                        })
                        .asLongStream()
                        .sum())
                .get();
    }


}

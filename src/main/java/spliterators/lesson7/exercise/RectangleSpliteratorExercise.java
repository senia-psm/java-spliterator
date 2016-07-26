package spliterators.lesson7.exercise;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class RectangleSpliteratorExercise {

    @Param({"100"})
    public int outerLength;

    @Param({"100"})
    public int innerLength;

    public int[][] array;

    @Setup
    public void setup() {
        array = new int[outerLength][];

        for (int i = 0; i < array.length; i++) {
            int[] inner = new int[innerLength];
            array[i] = inner;
            for (int j = 0; j < inner.length; j++) {
                inner[j] = ThreadLocalRandom.current().nextInt();
            }
        }
    }


    @Benchmark
    public long baiseline_seq() {
        return Arrays.stream(array)
                .sequential()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long baiseline_par() {
        return Arrays.stream(array)
                .parallel()
                .flatMapToInt(Arrays::stream)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long rectangle_seq() {
        final boolean parallel = false;
        return StreamSupport.intStream(new RectangleSpliterator(array), parallel)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long rectangle_par() {
        final boolean parallel = true;
        return StreamSupport.intStream(new RectangleSpliterator(array), parallel)
                .asLongStream()
                .sum();
    }
}

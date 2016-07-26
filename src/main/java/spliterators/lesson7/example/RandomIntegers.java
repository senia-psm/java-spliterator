package spliterators.lesson7.example;


import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
public class RandomIntegers {

    @Param({"100000"})
    public int count;

    @Param({"0", "10", "100", "1000"})
    public long consume;

    public Iterator<Integer> createIterator() {
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return getAnInt();
            }
        };
    }

    private int getAnInt() {
        Blackhole.consumeCPU(consume);
        return ThreadLocalRandom.current().nextInt();
    }

    @Benchmark
    public long classic() {
        final Iterator<Integer> iterator = createIterator();
        final int count = this.count;
        long result = 0;
        for (int i = 0; i < count; i++) {
            result += iterator.next();
        }

        return result;
    }

    @Benchmark
    public long fromIterator_seq() {
        final Iterator<Integer> iterator = createIterator();
        final Spliterator<Integer> spliterator =
                Spliterators.spliteratorUnknownSize(iterator, (Spliterator.CONCURRENT | Spliterator.NONNULL));

        final boolean parallel = false;
        return StreamSupport.stream(spliterator, parallel)
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long fromIterator_par() {
        final Iterator<Integer> iterator = createIterator();
        final Spliterator<Integer> spliterator =
                Spliterators.spliteratorUnknownSize(iterator, (Spliterator.CONCURRENT | Spliterator.NONNULL));

        final boolean parallel = true;
        return StreamSupport.stream(spliterator, parallel)
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long spliterator_seq() {
        final boolean parallel = false;
        return StreamSupport.stream(new RandomIntegerSpliterator(), parallel)
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();

    }

    @Benchmark
    public long spliterator_par() {
        final boolean parallel = true;
        return StreamSupport.stream(new RandomIntegerSpliterator(), parallel)
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();

    }

    private class RandomIntegerSpliterator extends Spliterators.AbstractSpliterator<Integer> {
        private long estimation;

        private RandomIntegerSpliterator(long estimation) {
            super(estimation, (Spliterator.CONCURRENT | Spliterator.NONNULL | Spliterator.IMMUTABLE));
            this.estimation = estimation;
        }

        public RandomIntegerSpliterator() {
            this(Long.MAX_VALUE);
        }

        @Override
        public boolean tryAdvance(Consumer<? super Integer> action) {
            action.accept(getAnInt());
            return true;
        }

        @Override
        public Spliterator<Integer> trySplit() {
            estimation >>>= 1;
            return new RandomIntegerSpliterator(estimation);
        }

        @Override
        public long estimateSize() {
            return estimation;
        }
    }

    @Benchmark
    public long generate_seq() {
        return Stream.generate(this::getAnInt)
                .sequential()
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();
    }

    @Benchmark
    public long generate_par() {
        return Stream.generate(this::getAnInt)
                .parallel()
                .limit(count)
                .mapToInt(i -> i)
                .asLongStream()
                .sum();
    }
}

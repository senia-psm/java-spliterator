package spliterators.lesson9.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithArraySpliterator<A, B> extends Spliterators.AbstractSpliterator<Pair<A, B>> {


    private final Spliterator<A> inner;
    private final B[] array;

    public ZipWithArraySpliterator(Spliterator<A> inner, B[] array) {
        super(Long.MAX_VALUE, 0); // FIXME:
        // TODO
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean tryAdvance(Consumer<? super Pair<A, B>> action) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super Pair<A, B>> action) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<Pair<A, B>> trySplit() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        // TODO
        throw new UnsupportedOperationException();
    }
}

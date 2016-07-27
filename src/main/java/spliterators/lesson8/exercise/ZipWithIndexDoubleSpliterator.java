package spliterators.lesson8.exercise;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ZipWithIndexDoubleSpliterator extends Spliterators.AbstractSpliterator<IndexedDoublePair> {


    private final OfDouble inner;
    private final int currentIndex;

    public ZipWithIndexDoubleSpliterator(long est, int additionalCharacteristics, OfDouble inner) {
        this(0, inner);
    }

    private ZipWithIndexDoubleSpliterator(int firstIndex, OfDouble inner) {
        super(inner.estimateSize(), inner.characteristics());
        currentIndex = firstIndex;
        this.inner = inner;
    }

    @Override
    public boolean tryAdvance(Consumer<? super IndexedDoublePair> action) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEachRemaining(Consumer<? super IndexedDoublePair> action) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<IndexedDoublePair> trySplit() {
        // TODO
        // if (inner.hasCharacteristics(???)) {
        //   use inner.trySplit
        // } else

        return super.trySplit();
    }

    @Override
    public long estimateSize() {
        // TODO
        throw new UnsupportedOperationException();
    }
}

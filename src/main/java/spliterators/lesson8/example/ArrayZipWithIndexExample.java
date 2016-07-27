package spliterators.lesson8.example;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class ArrayZipWithIndexExample {

    public static class IndexedAttaySpliterator<T> extends Spliterators.AbstractSpliterator<IndexedPair<T>> {


        private final T[] array;
        private int startInclusive;
        private final int endExclusive;

        public IndexedAttaySpliterator(T[] array) {
            this(array, 0, array.length);
        }

        private IndexedAttaySpliterator(T[] array, int startInclusive, int endExclusive) {
            super(endExclusive - startInclusive,
                    Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
            this.array = array;
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public boolean tryAdvance(Consumer<? super IndexedPair<T>> action) {
            if (startInclusive < endExclusive) {
                action.accept(new IndexedPair<>(startInclusive, array[startInclusive]));
                startInclusive += 1;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super IndexedPair<T>> action) {
            for (int i = startInclusive; i < endExclusive; i++) {
                action.accept(new IndexedPair<>(i, array[i]));
            }
        }

        @Override
        public long estimateSize() {
            return endExclusive - startInclusive;
        }

        @Override
        public IndexedAttaySpliterator<T> trySplit() {
            int length = endExclusive - startInclusive;
            if (length <= 1) {
                return null;
            }

            int middle = startInclusive + length/2;

            final IndexedAttaySpliterator<T> newSpliterator = new IndexedAttaySpliterator<>(array, startInclusive, middle);

            startInclusive = middle;

            return newSpliterator;
        }
    }
}

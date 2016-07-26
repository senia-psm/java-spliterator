package spliterators.lesson7.example;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class ArrayExample {
    public static class IntArraySpliterator extends Spliterators.AbstractIntSpliterator {

        private final int[] array;
        private int startInclusive;
        private final int endExclusive;

        public IntArraySpliterator(int[] array) {
            this(array, 0, array.length);
        }

        private IntArraySpliterator(int[] array, int startInclusive, int endExclusive) {
            super(endExclusive - startInclusive,
                    Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);
            this.array = array;
            this.startInclusive = startInclusive;
            this.endExclusive = endExclusive;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (startInclusive < endExclusive) {
                action.accept(array[startInclusive]);
                startInclusive += 1;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public long estimateSize() {
            return endExclusive - startInclusive;
        }

        @Override
        public OfInt trySplit() {
            int length = endExclusive - startInclusive;
            if (length <= 1) {
                return null;
            }

            int middle = startInclusive + length/2;

            final IntArraySpliterator newSpliterator = new IntArraySpliterator(array, startInclusive, middle);

            startInclusive = middle;

            return newSpliterator;
        }
    }
}

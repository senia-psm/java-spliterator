package spliterators.lesson7.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private static int getColumnCount(int[][] array) {
        return (array.length == 0) ? 0 : array[0].length;
    }

    private static int getArraySize(int[][] array) {
        return array.length * getColumnCount(array);
    }

    private final int[][] array;
    private final int columnCount;
    private final int finalIndexExc;
    private int currentRow;
    private int currentCol;
    private int currentIndex;
    private boolean noMoreElements;

    private void setCurrentIndex(int absoluteIndex) {
        currentRow = absoluteIndex / columnCount;
        currentCol = absoluteIndex % columnCount;
        currentIndex = absoluteIndex;
        noMoreElements = absoluteIndex >= finalIndexExc;
    }

    public RectangleSpliterator(int[][] array) {
        this(array, 0, getArraySize(array));
    }

    private RectangleSpliterator(int[][] array, int fromAbsoluteIndexInc, int toAbsoluteIndexExc) {
        super(toAbsoluteIndexExc - fromAbsoluteIndexInc,
                Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        this.array = array;
        columnCount = getColumnCount(array);
        finalIndexExc = toAbsoluteIndexExc;
        setCurrentIndex(fromAbsoluteIndexInc);
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (noMoreElements) return false;

        try {
            action.accept(array[currentRow][currentCol]);
        } finally {
            if (++currentCol == columnCount) {
                currentCol = 0;
                currentRow++;
            }
            noMoreElements = (++currentIndex == finalIndexExc);
        }
        return true;
    }

    @Override
    public OfInt trySplit() {
        if (finalIndexExc - currentIndex < 2) return null;

        int splitPosition = currentIndex + (finalIndexExc - currentIndex) / 2;
        RectangleSpliterator newSpliterator = new RectangleSpliterator(array, currentIndex, splitPosition);
        setCurrentIndex(splitPosition);
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return finalIndexExc - currentIndex;
    }

}

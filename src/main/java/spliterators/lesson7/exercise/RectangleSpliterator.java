package spliterators.lesson7.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {

    private final int innerLength;
    private final int[][] array;
    private final int startOuterInclusive;
    private final int endOuterExclusive;
    private final int startInnerInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        innerLength = array.length == 0 ? 0 : array[0].length;
        this.array = array;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    private boolean isSplitterable(){
        return endOuterExclusive-startInnerInclusive>=2;
    }

    @Override
    public OfInt trySplit() {
        final int aboutMiddleIndex = startInnerInclusive + (endOuterExclusive-startInnerInclusive)/2;

        RectangleSpliterator rectangleSpliterator = new RectangleSpliterator(array,
                startOuterInclusive,
                endOuterExclusive,
                aboutMiddleIndex);
                this.startInnerInclusive = aboutMiddleIndex;        //how???? its final
        return isSplitterable()?rectangleSpliterator:null;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive)*innerLength - startInnerInclusive;
    }

    private int currentX;
    private int currentY;
    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (currentY>array.length && currentX>array[currentY].length)

        if (startInnerInclusive<endOuterExclusive){
            action.accept(array[currentY][currentX]);
            currentX++;
            if(currentX>=array[currentY].length){
                currentX=0;
                currentY++;
            }
        }
        return true;
    }
}

package spliterators.lesson8.example;

import org.junit.Test;
import spliterators.lesson8.example.ArrayZipWithIndexExample.IndexedAttaySpliterator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class IndexedAttaySpliteratorTest {

    private String[] getRandomArray(int length) {
        final String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = String.valueOf(ThreadLocalRandom.current().nextInt());
        }

        return result;
    }

    @Test
    public void comparePaired() {
        final String[] randomArray = getRandomArray(1000);

        final List<IndexedPair<String>> result1 =
                Stream.iterate(
                        new IndexedPair<>(0, randomArray[0]),
                        p -> new IndexedPair<>(p.getIndex() + 1, randomArray[p.getIndex() + 1]))
                        .limit(randomArray.length)
                        .collect(toList());

        final List<IndexedPair<String>> result2 =
        StreamSupport.stream(new IndexedAttaySpliterator<>(randomArray), true)
                .map(p -> new IndexedPair<>(p.getIndex() + 1, p.getValue()))
                .map(p -> new IndexedPair<>(p.getIndex() - 1, p.getValue()))
                .collect(toList());

        assertEquals(result1, result2);

    }
}

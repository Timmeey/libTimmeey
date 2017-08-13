package de.timmeey.libTimmeey.Iterable;

import de.timmeey.libTimmeey.Func.UncheckedBiFunc;
import de.timmeey.libTimmeey.math.LinearSteppingTransform;
import java.util.Arrays;
import java.util.Iterator;
import org.cactoos.iterator.Mapped;
import org.junit.Test;

/**
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 * @since ${VERSION}
 */
public class EntryTransformingIteratorTest {
    /**
     * ${CLASS} can work.
     * @throws Exception If fails
     */
    @Test
    public void testNext() throws Exception {
        Iterator<Long> start = Arrays.asList(0l, 50l, 0l, 0l).iterator();
        Iterator<Long> end = Arrays.asList(100l, 100l, 100l, 100l).iterator();
        final EntryTransformingIterator<Long> underTest = new
            EntryTransformingIterator<>(start, end,
            new UncheckedBiFunc<>(
                (aLong, aLong2) ->
                    new Mapped<>(
                        new LinearSteppingTransform(aLong
                            .doubleValue(), aLong2.doubleValue(), 10l),
                        Double::longValue)));

        while (underTest.hasNext()) {
            StringBuffer sb = new StringBuffer("[");
            underTest.next().forEachRemaining(str -> sb.append(str).append("," +
                " "));
            sb.append("]");
            System.out.println(sb);
        }
    }

}

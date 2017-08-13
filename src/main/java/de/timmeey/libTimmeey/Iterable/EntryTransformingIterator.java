package de.timmeey.libTimmeey.Iterable;

import de.timmeey.libTimmeey.Func.UncheckedBiFunc;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import lombok.NonNull;

/**
 * EntryTransformingIterator.
 * Transformes a List of values to a target List.
 * Example
 * [0,0,100] should be transformed to [100,100,100]
 * Now you give it a function that is applied to every element to transform it
 * into its own iterator. E.g. LinearSteppingTransofmration with 100 Steps.
 * That would mean
 * you would get an Iterator that gives you
 * [0,0,100]->[100,100,100] = [[1,1,100],[2,2,100],[3,3,100]...]
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class EntryTransformingIterator<V> implements
    Iterator<Iterator<V>> {
    private final Iterable<Iterator<V>> result;

    /**
     * The buffer storing the objects of the iterator.
     */
    private final Queue<Iterator<V>> buffer;

    public EntryTransformingIterator(@NonNull final Iterator<V> start,
        @NonNull final Iterator<V>
            end, final UncheckedBiFunc<V, V, Iterator<V>> func) {
        this.buffer = new LinkedList<>();

        final LinkedList<Iterator<V>> list = new LinkedList<>();
        start.forEachRemaining((i) -> list.add(func.apply(i, end.next())));
        this.result = list;
    }

    @Override
    public boolean hasNext() {
        if (this.buffer.isEmpty()) {
            try {
                List<V> tmp = new LinkedList<>();
                for (Iterator<V> iter : result) {
                    tmp.add(iter.next());
                }
                this.buffer.add(tmp.iterator());
            } catch (NoSuchElementException ex) {
            }
        }
        return !this.buffer.isEmpty();
    }

    @Override
    public Iterator<V> next() {
        if (hasNext()) {
            return this.buffer.poll();
        } else {
            throw new NoSuchElementException("Iterator has no more elements");
        }
    }
}

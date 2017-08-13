package de.timmeey.libTimmeey.math;

import de.timmeey.libTimmeey.SafeStickyScalar;
import java.util.Iterator;
import org.cactoos.Func;
import org.cactoos.iterable.Mapped;
import org.cactoos.iterable.NaturalNumbers;

/**
 * SteppingTransform.
 * Iterator containing exactly STEPS entries.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SteppingTransform<T> implements Iterator<T> {

    private final SafeStickyScalar<Iterator<T>> result;

    public SteppingTransform(final long steps, final Func<Long, T>
        singleStepTransformation) {
        this.result = new SafeStickyScalar<>(() ->
            new Mapped<>(new NaturalNumbers(1, steps + 1),
                singleStepTransformation)
                .iterator());
    }

    @Override
    public boolean hasNext() {
        return this.result.value().hasNext();
    }

    @Override
    public T next() {
        return this.result.value().next();
    }
}

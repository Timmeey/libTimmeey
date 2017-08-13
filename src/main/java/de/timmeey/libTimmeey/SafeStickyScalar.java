package de.timmeey.libTimmeey;

import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * SafeStickyScalar.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class SafeStickyScalar<T> implements Scalar<T> {

    private final UncheckedScalar<T> src;

    public SafeStickyScalar(final Scalar<T> src) {
        this.src = new UncheckedScalar<>(new StickyScalar<>(src));
    }

    @Override
    public T value() {
        return src.value();
    }
}

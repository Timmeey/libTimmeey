package de.timmeey.libTimmeey.random;

import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * RandomNumber.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 1.0.1
 */
public final class RandomNumber implements Scalar<Integer> {

    private final UncheckedScalar<Integer> src;

    public RandomNumber(int min, int max) {
        src = new UncheckedScalar<>(
            new StickyScalar<>(
                () -> between(min, max)
            )
        );
    }

    private int between(final int min, final int max) {
        double rand = Math.random();
        while (rand * max < min || rand * max > max) {
            rand = Math.random();
        }
        return (int) (rand * max);
    }

    @Override
    public Integer value() {
        return src.value();
    }
}

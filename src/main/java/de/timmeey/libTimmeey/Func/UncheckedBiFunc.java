package de.timmeey.libTimmeey.Func;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.cactoos.BiFunc;

/**
 * UncheckedBiFunc.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class UncheckedBiFunc<X, Y, Z> implements BiFunc<X, Y, Z> {

    /**
     * Original func.
     */
    private final BiFunc<X, Y, Z> func;

    /**
     * Ctor.
     * @param fnc Encapsulated func
     */
    public UncheckedBiFunc(final BiFunc<X, Y, Z> fnc) {
        this.func = fnc;
    }

    @Override
    public Z apply(final X first, final Y second) {
        try {
            return new IoCheckedBiFunc<>(this.func).apply(first, second);
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}

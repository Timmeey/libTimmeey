package de.timmeey.libTimmeey.Func;

import java.io.IOException;
import org.cactoos.BiFunc;

/**
 * IOCheckedBiFunc.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class IoCheckedBiFunc<X, Y, Z> implements BiFunc<X, Y, Z> {

    private final BiFunc<X, Y, Z> func;

    public IoCheckedBiFunc(BiFunc<X, Y, Z> fnc) {
        this.func = fnc;
    }

    @Override
    public Z apply(final X first, final Y second) throws IOException {
        try {
            return this.func.apply(first, second);
        } catch (IOException var3) {
            throw var3;
        } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
            throw new IOException(var4);
        } catch (Exception var5) {
            throw new IOException(var5);
        }
    }
}

package de.timmeey.libTimmeey.printable;

import java.util.Iterator;

/**
 * JsonPrintable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Printed {
    default Printed with(String key, long value) {
        return this.with(key, value + "");
    }

    Printed with(String key, String value);

    default Printed with(String key, int value) {
        return this.with(key, value + "");
    }

    default Printed with(String key, boolean value) {
        return this.with(key, value + "");
    }

    default Printed with(String key, double value) {
        return this.with(key, value + "");
    }

    default Printed with(String key, float value) {
        return this.with(key, value + "");
    }

    default Printed with(String key, Printable value) {
        return this.with(key, value + "");
    }

    default Printed onlyList(Iterable<? extends Printable> value) {
        return this.onlyList(value.iterator());
    }

    Printed onlyList(Iterator<? extends Printable> value);

    default Printed withList(String key, Iterable<? extends
        Printable> value) {
        return this.withList(key, value.iterator());
    }

    Printed withList(String key, Iterator<? extends
        Printable> value);

    String asString();
}

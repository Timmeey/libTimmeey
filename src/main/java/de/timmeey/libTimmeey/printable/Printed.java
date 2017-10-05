package de.timmeey.libTimmeey.printable;

import java.util.Iterator;
import java.util.Map;
import org.cactoos.iterable.ListOf;

/**
 * JsonPrintable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Printed {
    Printed with(String key, long value);

    Printed with(String key, String value);

    Printed with(String key, int value);

    Printed with(String key, boolean value);

    Printed with(String key, double value);

    Printed with(String key, float value);

    Printed with(String key, Printable value);

    Printed only(Iterable<? extends Printable> value);

    Printed only(Iterator<? extends Printable> value);

    default Printed with(String key, Iterable<? extends
        Printable> value) {
        return this.with(key, value.iterator());
    }

    Printed with(String key, Map<?, ? extends Printable> value);


    default Printed only(Map<?, ?> value) {
        final Object type = value.values().stream().findAny().get();
        if (type instanceof Printable) {
            value.forEach((key, entry) -> this.with(key.toString(), (Printable)entry));
        }else if(type instanceof Iterable){
            value.forEach((key, entry) -> this.with(key.toString(), (Iterable)entry));
        }else if(type instanceof Iterator){
            value.forEach((key, entry) -> this.with(key.toString(), (Iterator)entry));
        }else if(type instanceof Map){
            value.forEach((key, entry) -> this.with(key.toString(), (Map)entry));
        }

        return this;
    }

    Printed with(String key, Iterator<? extends
        Printable> value);

    String asString();

    class IsPrintable {
        public boolean isPrintable(Object o) {
            if (o instanceof Printable) {
                return true;
            }
            if (o instanceof Iterable && new ListOf<>((Iterable) o).stream()
                .allMatch(p -> this.isPrintable(p))) {
                return true;
            }

            if (o instanceof Map && ((Map) o).values().stream().allMatch(p ->
                this.isPrintable(p))) {
                return true;
            }
            return false;
        }

        public String print(Object o, Printed printed) {
            if (!isPrintable(o)) {
                return o.toString();
            }

            if (o instanceof Printable) {
                return ((Printable) o).print(printed)
                    .asString();
            } else if (o instanceof Iterable) {
                return printed.only((Iterable<Printable>) o)
                    .asString();
            } else if (o instanceof Map) {
                printed.only((Map) o);
                return printed.asString();
            }
            return "";

        }
    }
}

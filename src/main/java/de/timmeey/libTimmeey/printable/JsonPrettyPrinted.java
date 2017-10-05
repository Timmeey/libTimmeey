package de.timmeey.libTimmeey.printable;

import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JsonPrintable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public final class JsonPrettyPrinted implements Printed {

    private final JSONObject jsonObject;
    private final JSONArray jsonArray;

    public JsonPrettyPrinted() {
        this.jsonObject = new JSONObject();
        this.jsonArray = new JSONArray();
    }

    @Override
    public Printed with(final String key, final String value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final int value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final long value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final boolean value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final double value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final float value) {
        this.jsonObject.put(key, value);
        return this;
    }

    @Override
    public Printed with(final String key, final Printable value) {
        this.jsonObject.put(key, ((JsonPrettyPrinted) value.print(new
            JsonPrettyPrinted())).json());
        return this;
    }

    @Override
    public Printed only(final Iterable<? extends Printable> value) {
        return this.only(value.iterator());
    }

    @Override
    public Printed only(final Iterator<? extends Printable> value) {
        value.forEachRemaining(printable -> this.jsonArray.put((
            (JsonPrettyPrinted) printable.print(new JsonPrettyPrinted()))
            .json()));
        return this;
    }

    @Override
    public Printed with(final String key, final Map<?, ? extends Printable>
        map) {
        JSONArray array = new JSONArray();
        map.forEach((mapKey, mapValue) -> array.put(new JsonPrettyPrinted().with(key,mapValue)));
        this.jsonObject.put(key, array);
        return this;
    }

    @Override
    public Printed with(final String key, final Iterator<? extends
        Printable> value) {
        JSONArray array = new JSONArray();
        value.forEachRemaining(printable -> array.put(((JsonPrettyPrinted)
            printable.print(new JsonPrettyPrinted())).json()));
        this.jsonObject.put(key, array);
        return this;
    }

    @Override
    public String asString() {

        if (this.jsonObject.length() != 0 && this.jsonArray.length() != 0) {
            throw new IllegalStateException("Only either can be used. It can " +
                "be a json list OR it can be a complex json object, but not " +
                "both");
        } else if (this.jsonObject.length() != 0) {
            return this.jsonObject.toString(2);
        } else {
            return this.jsonArray.toString(2);
        }
    }

    protected JSONObject json() {
        return this.jsonObject;
    }
}

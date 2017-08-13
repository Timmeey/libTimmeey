package de.timmeey.libTimmeey.printable;

import java.util.Iterator;
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
    public Printed with(final String key, final Printable value) {
        this.jsonObject.put(key, ((JsonPrettyPrinted) value.print(new
            JsonPrettyPrinted())).json());
        return this;
    }

    @Override
    public Printed onlyList(final Iterator<? extends Printable> value) {
        value.forEachRemaining(printable -> this.jsonArray.put((
            (JsonPrettyPrinted) printable.print(new JsonPrettyPrinted()))
            .json()));
        return this;
    }

    @Override
    public Printed withList(final String key, final Iterator<? extends
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

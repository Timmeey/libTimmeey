package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import org.cactoos.Func;

/**
 * RejectingSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public final class RejectingSensor<T> implements Sensor<T> {

    private final Sensor<T> src;
    private final Func<Reading<T>, Boolean> guard;

    public RejectingSensor(final Sensor<T> src, final Func<Reading<T>,
        Boolean> guard) {
        this.src = src;
        this.guard = guard;
    }

    @Override
    public void addReading(final Reading<T> reading) throws Exception {
        if (this.guard.apply(reading)) {
            this.src.addReading(reading);
        } else {
            throw new IllegalArgumentException("Reading %s did not fulfill " +
                "requirements");
        }
    }

    @Override
    public Iterable<Reading<T>> readings() throws Exception {
        return this.src.readings();
    }

    @Override
    public void delete(final Reading<T> reading) throws Exception {
        this.src.delete(reading);
    }
}

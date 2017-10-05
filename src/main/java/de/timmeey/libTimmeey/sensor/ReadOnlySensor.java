package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import lombok.RequiredArgsConstructor;

/**
 * ReadOnlySensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
@RequiredArgsConstructor
public final class ReadOnlySensor<T> implements Sensor<T> {

    private final Sensor<T> src;

    @Override
    public Iterable<Reading<T>> readings() throws Exception {
        return this.src.readings();
    }

    @Override
    public void addReading(final Reading<T> reading) throws Exception {
        throw new UnsupportedOperationException("Adding a Reading is not " +
            "supported by a ReadOnly sensor");
    }

    @Override
    public void delete(final Reading<T> reading) throws Exception {
        throw new UnsupportedOperationException("Deleting a Reading is not " +
            "supported by a ReadOnly sensor");
    }
}

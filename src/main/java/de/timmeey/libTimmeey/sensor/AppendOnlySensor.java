package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import lombok.RequiredArgsConstructor;

/**
 * AppendOnlySensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 02.
 */
@RequiredArgsConstructor
public final class AppendOnlySensor<T> implements Sensor<T> {

    private final Sensor<T> src;

    @Override
    public Iterable<Reading<T>> readings() throws Exception {
        return this.src.readings();
    }

    @Override
    public void addReading(final Reading<T> reading) throws Exception {
        this.src.addReading(reading);
    }

    @Override
    public void delete(final Reading<T> reading) {
        throw new UnsupportedOperationException("Deleting a Reading is not " +
            "supported by a AppendOnly sensor");
    }
}

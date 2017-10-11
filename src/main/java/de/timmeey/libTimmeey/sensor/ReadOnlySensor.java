package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * ReadOnlySensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
@RequiredArgsConstructor
public final class ReadOnlySensor implements Sensor {

    @Delegate(excludes = ReadOnlySensor.Immutable.class)
    private final Sensor src;

    @Override
    public Reading addReading(final double value, final ZonedDateTime datetime)
        throws Exception {
        throw new UnsupportedOperationException("Adding a Reading is not " +
            "supported by a ReadOnly sensor");
    }

    @Override
    public void delete(final Reading reading) throws Exception {
        throw new UnsupportedOperationException("Deleting a Reading is not " +
            "supported by a ReadOnly sensor");
    }

    private interface Immutable {
        public void addReading(final double value, final ZonedDateTime
            datetime) throws Exception;

        public void delete(final Reading reading) throws Exception;

    }
}

package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Sensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Sensor {

    /**
     * Returns all readings of this sensor
     * No guarantees are made about thread safety
     * @return all previous readings of this sensor
     */
    Iterable<Reading> readings() throws Exception;

    Optional<Reading> last() throws Exception;

    Reading addReading(double value, ZonedDateTime datetime) throws Exception;

    void delete(Reading reading) throws Exception;

    String unit();

}

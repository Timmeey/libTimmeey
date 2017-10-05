package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;

/**
 * Sensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public interface Sensor<T> {

    Iterable<Reading<T>> readings() throws Exception;

    void addReading(Reading<T> reading) throws Exception;

    void delete(Reading<T> reading) throws Exception;

}

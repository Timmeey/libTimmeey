package de.timmeey.libTimmeey.sensor.reading;

import java.time.ZonedDateTime;

/**
 * Reading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public interface Reading<T> {

    T value();

    ZonedDateTime datetime();
}

package de.timmeey.libTimmeey.sensor.reading;

import de.timmeey.libTimmeey.persistence.UniqueIdentifier;
import java.time.ZonedDateTime;

/**
 * Reading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public interface Reading {

    UniqueIdentifier id();

    double value();

    ZonedDateTime datetime();
}

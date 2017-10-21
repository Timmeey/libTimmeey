package de.timmeey.libTimmeey.sensor.reading;

import de.timmeey.libTimmeey.printable.Printable;
import java.time.ZonedDateTime;

/**
 * Reading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public interface Reading extends Printable {

    double value();

    ZonedDateTime datetime();
}

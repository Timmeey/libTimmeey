package de.timmeey.libTimmeey.sensor.reading;

import de.timmeey.libTimmeey.printable.Printed;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * FkReading.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@Accessors(fluent = true, chain = false)
@Data
public final class FkReading implements Reading {

    private final double value;
    private final ZonedDateTime datetime;

    @Override
    public Printed print(final Printed printed) {
        throw new UnsupportedOperationException("#print()");
    }
}

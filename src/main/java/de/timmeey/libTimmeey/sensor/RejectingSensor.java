package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import lombok.experimental.Delegate;
import org.cactoos.BiFunc;

/**
 * RejectingSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public final class RejectingSensor implements Sensor {

    @Delegate(excludes = Add.class)
    private final Sensor src;
    private final BiFunc<Double, ZonedDateTime, Boolean> guard;

    public RejectingSensor(final Sensor src, final BiFunc<Double,
        ZonedDateTime, Boolean> guard) {
        this.src = src;
        this.guard = guard;
    }

    @Override
    public Reading addReading(double value, ZonedDateTime datetime) throws
        Exception {
        if (this.guard.apply(value, datetime)) {
            return this.src.addReading(value, datetime);
        } else {
            throw new IllegalArgumentException("Reading did not fulfill " +
                "requirements");
        }
    }

    private interface Add {
        void addReading(double value, ZonedDateTime datetime) throws Exception;
    }
}

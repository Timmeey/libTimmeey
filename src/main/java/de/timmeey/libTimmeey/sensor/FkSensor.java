package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.persistence.UUIDUniqueIdentifier;
import de.timmeey.libTimmeey.sensor.reading.FkReading;
import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * FkSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
@RequiredArgsConstructor
public class FkSensor implements Sensor {

    private final List<Reading> persistence = new LinkedList<>();
    private final String unit;

    public FkSensor() {
        this.unit = "FkUnit";
    }

    @Override
    public Iterable<Reading> readings() throws Exception {
        return Collections.unmodifiableList(this.persistence);
    }

    @Override
    public Optional<Reading> last() throws Exception {
        return Optional.ofNullable(this.persistence.get(
            this.persistence.size() - 1)
        );
    }

    @Override
    public Reading addReading(final double value, final ZonedDateTime datetime)
        throws Exception {
        val result = new FkReading(new UUIDUniqueIdentifier(), value, datetime);
        this.persistence.add(result);
        return result;
    }

    @Override
    public void delete(final Reading reading) throws Exception {
        this.persistence.remove(reading);
    }

    @Override
    public String unit() {
        return this.unit;
    }
}

package de.timmeey.libTimmeey.sensor;

import de.timmeey.libTimmeey.sensor.reading.Reading;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * FkSensor.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class FkSensor<T> implements Sensor<T> {

    private final List<Reading<T>> persistence = new LinkedList<>();

    @Override
    public Iterable<Reading<T>> readings() throws Exception {
        return Collections.unmodifiableList(this.persistence);
    }

    @Override
    public void addReading(final Reading<T> reading) throws Exception {
        this.persistence.add(reading);
    }

    @Override
    public void delete(final Reading<T> reading) throws Exception {
        this.persistence.remove(reading);
    }
}

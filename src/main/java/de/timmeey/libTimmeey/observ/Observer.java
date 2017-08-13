package de.timmeey.libTimmeey.observ;

/**
 * Observer.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 1.0.1
 */
@FunctionalInterface
public interface Observer<Input> {

    void update(Input change);
}

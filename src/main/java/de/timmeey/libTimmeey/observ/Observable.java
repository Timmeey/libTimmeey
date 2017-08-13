package de.timmeey.libTimmeey.observ;

/**
 * Observable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 1.0.1
 */
public interface Observable<Output> {

    void register(Observer<Output> o);

    void unregister(Observer o);

}

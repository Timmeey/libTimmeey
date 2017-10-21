package de.timmeey.libTimmeey.sql;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * SqlTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public interface SqlTable<T extends SqlColumn> {

    Iterable<T> columns();

    default Iterable<T> indexColumns() {
        return Collections.unmodifiableCollection(StreamSupport.stream(this
            .columns().spliterator(), false).filter(c -> c.isIndex()).collect
            (Collectors.toList()));
    }

    default Optional<T> primaryKeyCoulmn() {
        return StreamSupport.stream(this.columns().spliterator(), false)
            .filter(c -> c.isPrimaryIndex()).findAny();
    }

    Optional<T> primaryKey();

    String createTableQuery();

    String insertQuery();

    Optional<String> getRowByPrimaryIndexQuery();

    String name();
}

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
public interface SqlTable {

    Iterable<? extends SqlColumn> columns();

    default Iterable<? extends SqlColumn> indexColumns() {
        return Collections.unmodifiableCollection(StreamSupport.stream(this
            .columns().spliterator(), false).filter(c -> c.isIndex()).collect
            (Collectors.toList()));
    }

    default Optional<? extends SqlColumn> primaryKeyCoulmn() {
        return StreamSupport.stream(this.columns().spliterator(), false)
            .filter(c -> c.isPrimaryIndex()).findAny();
    }

    Optional<? extends SqlColumn> primaryKey();

    String createTableQuery();

    String insertQuery();

    Optional<String> getRowByPrimaryIndexQuery();

    String name();
}

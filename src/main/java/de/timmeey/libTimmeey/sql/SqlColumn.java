package de.timmeey.libTimmeey.sql;

import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * SqlColumn.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public interface SqlColumn<T extends SqlTable> {
    String name();

    String type();

    Iterable<String> attributes();

    boolean isPrimaryIndex();

    boolean isIndex();

    Optional<? extends SqlForeignKey> foreignKey(T childTable);

    default boolean hasAttribute(String attribute) {
        return StreamSupport.stream(
            this.attributes().spliterator(), false)
            .anyMatch(c -> c.equals(attribute));
    }

}


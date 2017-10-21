package de.timmeey.libTimmeey.sql;

/**
 * SqlForeignKey.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public interface SqlForeignKey<T extends SqlTable, C extends SqlColumn> {

    T childTable();

    C childReferenceColumn();

    T parentTable();

    C parentColumn();
}

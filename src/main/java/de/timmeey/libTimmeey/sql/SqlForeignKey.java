package de.timmeey.libTimmeey.sql;

/**
 * SqlForeignKey.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public interface SqlForeignKey {

    SqlTable childTable();

    SqlColumn childReferenceColumn();

    SqlTable parentTable();

    SqlColumn parentColumn();
}

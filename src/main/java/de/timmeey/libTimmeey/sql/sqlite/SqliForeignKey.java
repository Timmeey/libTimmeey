package de.timmeey.libTimmeey.sql.sqlite;

import de.timmeey.libTimmeey.sql.SqlColumn;
import de.timmeey.libTimmeey.sql.SqlForeignKey;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * SqliForeignKey.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
@Accessors(fluent = true)
@Getter
public class SqliForeignKey implements SqlForeignKey {

    private final SqliTable childTable, parentTable;
    private final SqliColumn childColumn, parentColumn;

    public SqliForeignKey(final SqliTable childTable, final SqliTable
        parentTable, final SqliColumn childColumn, final SqliColumn
        parentColumn) {

        this.childTable = childTable;
        this.parentTable = parentTable;
        this.childColumn = childColumn;
        this.parentColumn = parentColumn;
        this.validate();

    }

    private boolean validate() {
        if (!parentColumn.isPrimaryIndex()) {
            if (!parentColumn.hasAttribute(SqliAttribute.UNIQUE.toString())) {
                throw new IllegalArgumentException("Referencing parent Key " +
                    "column needs to be either PRIMARY KEY or UNIQUE");
            }
        }
        return true;
    }

    String bootstrapQuery() {
        return String.format("FOREIGN KEY(%s) REFERENCES %s(%s)", this
            .childColumn.name(), parentTable.name(), parentColumn().name());

    }

    @Override
    public SqlColumn childReferenceColumn() {
        return this.childColumn();
    }
}

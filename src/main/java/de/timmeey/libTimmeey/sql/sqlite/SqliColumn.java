package de.timmeey.libTimmeey.sql.sqlite;

import de.timmeey.libTimmeey.sql.SqlColumn;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SqliColumn.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.2
 */
public class SqliColumn implements SqlColumn<SqliTable> {
    private final String name;
    private final SqliColumn.SqliteDataType type;
    private final boolean isIndex;
    private final Collection<SqliAttribute> attributes;
    private final Optional<SqliTable> parentTable;
    private final Optional<SqliColumn> parentColumn;

    public SqliColumn(final String name, final SqliColumn.SqliteDataType
        type, final
    boolean isIndex, final SqliAttribute... attributes) {
        this.name = name;
        this.type = type;
        this.isIndex = isIndex;
        this.attributes = Arrays.asList(attributes);
        this.parentTable = Optional.empty();
        this.parentColumn = Optional.empty();
    }

    public SqliColumn(final String name, final SqliColumn.SqliteDataType
        type, final SqliTable parentTable, SqliColumn parentColumn, final
    SqliAttribute... attributes) {
        this.name = name;
        this.type = type;
        this.isIndex = true;
        this.parentTable = Optional.of(parentTable);
        this.parentColumn = Optional.of(parentColumn);
        this.attributes = Arrays.asList(attributes);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String type() {
        return this.type.toString();
    }

    @Override
    public Iterable<String> attributes() {
        return this.attributes.stream().map(a -> a.toString()).collect
            (Collectors.toList());
    }

    @Override
    public boolean isPrimaryIndex() {
        return attributes.stream().anyMatch(a -> a.equals(SqliAttribute
            .PRIMARY_KEY));
    }

    @Override
    public boolean isIndex() {
        return this.isIndex;
    }

    @Override
    public Optional<SqliForeignKey> foreignKey(SqliTable childTable) {
        if (this.isForeignKey()) {
            return Optional.of(new SqliForeignKey(
                    childTable,
                    parentTable.get(),
                    this,
                    parentColumn.get()
                )
            );
        } else {
            return Optional.empty();
        }
    }

    private boolean isForeignKey() {
        return this.parentColumn.isPresent() && this.parentTable.isPresent();
    }

    String asBootstrapQuery() {
        return String.format("%s %s %s", name, type, String.join("" +
            " ", attributesAsString()));

    }

    Optional<String> asIndexCreationQuery(SqliTable table) {
        if (this.isIndex()) {
            return Optional.of(String.format("CREATE INDEX %sindex ON %s(%s);" +
                    "\n",
                this.name(),
                table.name(),
                this.name()));
        } else {
            return Optional.empty();
        }

    }

    Collection<String> attributesAsString() {
        return this.attributes.stream().map(a -> a.toString()).collect
            (Collectors.toList());
    }

    public enum SqliteDataType {
        NULL, INTEGER, REAL, TEXT, BLOB
    }
}

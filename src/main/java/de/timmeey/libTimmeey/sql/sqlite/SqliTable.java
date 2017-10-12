package de.timmeey.libTimmeey.sql.sqlite;

import de.timmeey.libTimmeey.sql.SqlColumn;
import de.timmeey.libTimmeey.sql.SqlTable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SqliTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public class SqliTable implements SqlTable {

    private final Collection<SqliColumn> columns;
    private final String name;

    public SqliTable(final String name, final SqliColumn... columns) {
        this.columns = Arrays.asList(columns);
        this.name = name;
    }

    @Override
    public Iterable<? extends SqlColumn> columns() {
        return Collections.unmodifiableCollection(this.columns);
    }

    @Override
    public String insertQuery() {
        throw new UnsupportedOperationException("#insertQuery()");
    }

    @Override
    public String getRowByPrimaryIndexQuery() {
        throw new UnsupportedOperationException("#getRowByPrimaryIndexQuery()");
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String createTableQuery() {
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("CREATE TABLE %s(\n" +
                "%s,\n" +
                "%s\n" +
                ");\n",
            name,
            String.join(",\n", this.columnsAsStrings()),
            String.join(",\n", this.indexQueries()),
            String.join(",\n", this.foreignKeyQuery())
            )
        );
        return sb.toString();
    }

    private Collection<String> columnsAsStrings() {
        return this.columns.stream().map(c -> c.asBootstrapQuery()).collect
            (Collectors.toList());
    }

    private Collection<String> indexQueries() {
        return this.columns.stream().map(c -> c.asIndexCreationQuery(this))
            .filter(os -> os.isPresent()).map(iq -> iq.get()).collect
                (Collectors.toList());
    }

    private Collection<String> foreignKeyQuery() {
        return this.columns.stream().map(c -> c.foreignKey(this)).filter
            (Optional
            ::isPresent).map(Optional::get).map(c -> c.bootstrapQuery())
            .collect(Collectors.toList());
    }
}

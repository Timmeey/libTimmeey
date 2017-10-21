package de.timmeey.libTimmeey.sql.sqlite;

import de.timmeey.libTimmeey.sql.SqlTable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SqliTable.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 */
public class SqliTable implements SqlTable<SqliColumn> {

    private final Collection<SqliColumn> columns;
    private final String name;
    private final String insertQuery;
    private final Optional<String> getRowByPrimaryQuery;

    public SqliTable(final String name, final SqliColumn... columns) {
        this.columns = Arrays.asList(columns);
        this.name = name;
        this.insertQuery = String.format("INSERT INTO %s (%s) VALUES(%s)",
            this.name(), String
                .join(",", this.columns.stream().map(c -> c.name()).collect
                    (Collectors.toList())),
            String.join(", ", this.columns.stream().map(c -> "?").collect
                (Collectors.toList())));

        this.getRowByPrimaryQuery = primaryKey().map(c -> String.format
            ("SELECT * FROM %s WHERE %s=?", this.name(), c.name()));
    }

    @Override
    public Iterable<SqliColumn> columns() {
        return Collections.unmodifiableCollection(this.columns);
    }

    @Override
    public Optional<SqliColumn> primaryKey() {
        return this.columns.stream().filter(c -> c.isPrimaryIndex()).findAny();
    }

    @Override
    public String insertQuery() {
        return this.insertQuery;
    }

    @Override
    public Optional<String> getRowByPrimaryIndexQuery() {
        return this.getRowByPrimaryQuery;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String createTableQuery() {
        List<String> parts = new LinkedList<>();
        parts.addAll(columnsAsStrings());
        parts.addAll(foreignKeyQuery());

        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("CREATE TABLE %s(\n" +
                "   %s\n" +
                ");\n" +
                "%s;\n",
            name,
            String.join(",\n   ", parts),
            String.join(";\n", indexQueries())
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

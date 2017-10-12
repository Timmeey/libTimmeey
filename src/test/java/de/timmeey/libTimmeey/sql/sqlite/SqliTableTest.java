package de.timmeey.libTimmeey.sql.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteConfig;

/**
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id$
 */
public class SqliTableTest {
    private Connection conn;

    /**
     * ${CLASS} can work.
     * @throws Exception If fails
     */
    @Before
    public void setUp() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        conn = DriverManager.getConnection("jdbc:sqlite::memory:", config
            .toProperties());
        conn.setAutoCommit(true);
        if (execute("PRAGMA foreign_keys").getInt
            (1) != 1) {
            throw new IllegalStateException("Foreign key constrain not " +
                "enabled");
        }
    }

    @After
    public void tearDown() throws SQLException {
        this.conn.close();
    }

    @Test
    public void createTableQuerySinglePrimaryKey() throws Exception {
        SqliTable tmpTable = new SqliTable("testTable",
            new SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false,
                SqliAttribute.PRIMARY_KEY)
        );

        update(tmpTable.createTableQuery());
    }

    @Test
    public void createPrimaryKeyCoulmWithIndexFails() throws Exception {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() ->
                new SqliColumn("id", SqliColumn.SqliteDataType.TEXT, true,
                    SqliAttribute.PRIMARY_KEY));
    }

    @Test
    public void createTableWithForeignKeyFailsNotUnique() throws Exception {
        SqliColumn parentColumn = new SqliColumn("id", SqliColumn.SqliteDataType
            .TEXT, false);
        SqliTable parentTable = new SqliTable("parentTable", parentColumn);

        SqliTable childTable = new SqliTable("childTable",
            new SqliColumn("foreignId", SqliColumn.SqliteDataType.TEXT,
                parentTable, parentColumn, SqliAttribute.NOT_NULL)
        );
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy
            (() -> update(childTable.createTableQuery()));
    }

    @Test
    public void createTableWithForeignKeyUnique() throws Exception {
        SqliColumn parentColumn = new SqliColumn("id", SqliColumn.SqliteDataType
            .TEXT, false, SqliAttribute.UNIQUE);
        SqliTable parentTable = new SqliTable("parentTable", parentColumn);

        SqliTable childTable = new SqliTable("childTable",
            new SqliColumn("foreignId", SqliColumn.SqliteDataType.TEXT,
                parentTable, parentColumn, SqliAttribute.NOT_NULL)
        );
        update(childTable.createTableQuery());
    }

    @Test
    public void createTableWithForeignKeyWorks() throws Exception {
        SqliColumn parentColumn = new SqliColumn("id", SqliColumn.SqliteDataType
            .TEXT, false, SqliAttribute.PRIMARY_KEY);
        SqliTable parentTable = new SqliTable("parentTable", parentColumn);

        SqliTable childTable = new SqliTable("childTable",
            new SqliColumn("foreignId", SqliColumn.SqliteDataType.TEXT,
                parentTable, parentColumn, SqliAttribute.NOT_NULL)
        );

        update(parentTable.createTableQuery());
        update(childTable.createTableQuery());
    }

    @Test
    public void insertQuery() throws Exception {
        SqliTable tmpTable = new SqliTable("testTable",
            new SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false,
                SqliAttribute.PRIMARY_KEY),
            new SqliColumn("muh", SqliColumn.SqliteDataType.TEXT, true,
                SqliAttribute.UNIQUE, SqliAttribute.NOT_NULL)
        );

        update(tmpTable.createTableQuery());
        val prp = conn.prepareStatement(tmpTable.insertQuery());
        prp.setString(1, "foo");
        prp.setString(2, "barUnique");
        prp.execute();
    }

    @Test
    public void retrieveQuery() throws Exception {
        SqliTable tmpTable = new SqliTable("testTable",
            new SqliColumn("id", SqliColumn.SqliteDataType.TEXT, false,
                SqliAttribute.PRIMARY_KEY),
            new SqliColumn("muh", SqliColumn.SqliteDataType.TEXT, true,
                SqliAttribute.UNIQUE, SqliAttribute.NOT_NULL)
        );

        update(tmpTable.createTableQuery());
        val prp = conn.prepareStatement(tmpTable.insertQuery());
        prp.setString(1, "foo");
        prp.setString(2, "barUnique");
        prp.execute();

        val ret = conn.prepareStatement(tmpTable.getRowByPrimaryIndexQuery()
            .get());
        ret.setString(1, "foo");
        Assertions.assertThat(ret.executeQuery().getString("id")).isEqualTo
            ("foo");
    }

    private int update(String sql) throws SQLException {
        System.out.println(sql);
        val result = conn.createStatement().executeUpdate(sql);
        System.out.println(result);
        return result;
    }

    private ResultSet execute(String sql) throws SQLException {
        System.out.println(sql);
        val result = conn.createStatement().executeQuery(sql);
        result.getMetaData().getColumnCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.getMetaData().getColumnCount(); ++i) {
            sb.append(result.getMetaData().getColumnLabel(i + 1))
                .append(": ")
                .append(result.getString(i + 1));
        }
        System.out.println(sb.toString());
        return result;
    }

}

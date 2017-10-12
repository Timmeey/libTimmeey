package de.timmeey.libTimmeey.sql.sqlite;

public enum SqliAttribute {
    PRIMARY_KEY("PRIMARY KEY"), NOT_NULL("NOT NULL"), NULL("NULL"), UNIQUE
        ("UNIQUE");
    public final String representation;

    SqliAttribute(final String s) {
        this.representation = s;
    }
}

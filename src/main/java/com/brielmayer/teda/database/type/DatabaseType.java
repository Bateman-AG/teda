package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;

public interface DatabaseType extends Comparable<DatabaseType> {
    /**
     * @return The human-readable name for this database type.
     */
    String getName();

    /**
     * Check if this database type should handle the given JDBC url
     *
     * @param url The JDBC url.
     * @return {@code true} if this handles the JDBC url, {@code false} if not.
     */
    boolean handlesJDBCUrl(String url);

    public Database createDatabase(DatabaseConnection databaseConnection);
}

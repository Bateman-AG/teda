package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.MariaDatabase;
import com.brielmayer.teda.exception.TedaException;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class MariaDatabaseType implements DatabaseType {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:mariadb:") || url.startsWith("jdbc:p6spy:mariadb:");
    }

    @Override
    public Database createDatabase(DatabaseConnection databaseConnection) {
        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(databaseConnection.getUrl());
            dataSource.setUser(databaseConnection.getUser());
            dataSource.setPassword(databaseConnection.getPassword());
            return new MariaDatabase(dataSource);
        } catch (SQLException e) {
            throw new TedaException("Maria DB could not be connected: ", e.getMessage());
        }
    }

    @Override
    public int compareTo(DatabaseType o) {
        return 0;
    }
}

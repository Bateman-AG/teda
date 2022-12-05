package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.PostgresDatabase;
import org.postgresql.ds.PGSimpleDataSource;

public class PostgresDatabaseType implements DatabaseType {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:postgresql:") || url.startsWith("jdbc:p6spy:postgresql:");
    }

    @Override
    public Database createDatabase(DatabaseConnection databaseConnection) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(databaseConnection.getUrl());
        dataSource.setUser(databaseConnection.getUser());
        dataSource.setPassword(databaseConnection.getPassword());
        dataSource.setPortNumbers(new int[databaseConnection.getPort()]);
        return new PostgresDatabase(dataSource);
    }

    @Override
    public int compareTo(DatabaseType o) {
        return 0;
    }
}

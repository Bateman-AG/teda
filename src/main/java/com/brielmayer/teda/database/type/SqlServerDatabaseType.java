package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.SqlServerDatabase;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class SqlServerDatabaseType implements DatabaseType {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:sqlserver:") || url.startsWith("jdbc:jtds:") ||
                url.startsWith("jdbc:p6spy:sqlserver:") || url.startsWith("jdbc:p6spy:jtds:");
    }

    @Override
    public Database createDatabase(DatabaseConnection databaseConnection) {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(databaseConnection.getUrl());
        dataSource.setUser(databaseConnection.getUser());
        dataSource.setPassword(databaseConnection.getPassword());
        dataSource.setPortNumber(databaseConnection.getPort());
        return new SqlServerDatabase(dataSource);
    }

    @Override
    public int compareTo(DatabaseType o) {
        return 0;
    }
}

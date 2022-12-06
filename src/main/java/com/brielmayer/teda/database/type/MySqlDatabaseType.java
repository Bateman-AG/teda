package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.MySQLDatabase;
import com.mysql.cj.jdbc.MysqlDataSource;

public class MySqlDatabaseType implements DatabaseType {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:mysql:") || url.startsWith("jdbc:google:") ||
                url.startsWith("jdbc:p6spy:mysql:") || url.startsWith("jdbc:p6spy:google:");
    }

    @Override
    public Database createDatabase(DatabaseConnection databaseConnection) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(databaseConnection.getUrl());
        dataSource.setUser(databaseConnection.getUser());
        dataSource.setPassword(databaseConnection.getPassword());
        dataSource.setPort(databaseConnection.getPort());
        return new MySQLDatabase(dataSource);
    }

    @Override
    public int compareTo(DatabaseType o) {
        return 0;
    }
}

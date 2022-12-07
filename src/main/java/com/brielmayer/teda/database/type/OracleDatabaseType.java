package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.OracleDatabase;
import com.brielmayer.teda.exception.TedaException;
import oracle.jdbc.datasource.impl.OracleDataSource;

import java.sql.SQLException;

public class OracleDatabaseType implements DatabaseType {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:oracle") || url.startsWith("jdbc:p6spy:oracle");
    }

    @Override
    public Database createDatabase(DatabaseConnection databaseConnection) {
        try {
            OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(databaseConnection.getUrl());
            dataSource.setUser(databaseConnection.getUser());
            dataSource.setPassword(databaseConnection.getPassword());
            return new OracleDatabase(dataSource);
        } catch (SQLException e) {
            throw new TedaException("error create oracle connection: %s", e.getMessage());
        }
    }

    @Override
    public int compareTo(DatabaseType o) {
        return 0;
    }
}

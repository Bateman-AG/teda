package com.brielmayer.teda.database;

import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseConnection;

import javax.sql.DataSource;

public interface DatabaseType {

    boolean handlesDataSource(DataSource dataSource);
    BaseDatabase createDatabase(DataSource dataSource);
}

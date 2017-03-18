package com.brielmayer.teda.database;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.MySQLDatabase;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseTest {

    Database database;

    @Before
    public void initDatabase() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("teda");
        dataSource.setPassword("teda");
        dataSource.setDatabaseName("teda");
        database = new MySQLDatabase(dataSource);
    }

    @Test
    public void testTruncate() {

    }

    @Test
    public void testDrop() {

    }

}

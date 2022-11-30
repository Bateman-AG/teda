package com.brielmayer.teda.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MySQLDatabaseTest {

    Database database;

    @BeforeEach
    public void initDatabase() {
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

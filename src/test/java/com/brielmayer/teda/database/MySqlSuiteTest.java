package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MySqlSuiteTest {

    @Container
    public static MySQLContainer<?> mySqlContainer8_0_31 = new MySQLContainer<>("mysql:8.0.31");

    @Container
    public static MySQLContainer<?> mySqlContainer5_7_40 = new MySQLContainer<>("mysql:5.7.40");

    private BaseDatabase database;

    void initializeDatabase(MySQLContainer<?> container) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());
        database = DatabaseFactory.createDatabase(dataSource);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");
    }

    @Test
    void mySql8Test() {
        initializeDatabase(mySqlContainer8_0_31);
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }

    @Test
    void mySql5Test() {
        initializeDatabase(mySqlContainer5_7_40);
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }
}

package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.handler.LoadHandlerTest;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class MySqlSuiteTest {

    @Container
    public static MySQLContainer<?> mySqlContainer8_0_31 = new MySQLContainer<>("mysql:8.0.31");

    @Container
    public static MySQLContainer<?> mySqlContainer5_7_40 = new MySQLContainer<>("mysql:5.7.40");

    private BaseDatabase database;

    void initializeDatabase(MySQLContainer<?> container) {
        String jdbcUrl = container.getJdbcUrl();
        String user = container.getUsername();
        String password = container.getPassword();

        MysqlDataSource db = new MysqlDataSource();
        db.setPassword(password);
        db.setUrl(jdbcUrl);
        db.setUser(user);
        database = DatabaseFactory.createDatabase(db);

        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");
    }

    @Test
    void mySql8Test() {
        initializeDatabase(mySqlContainer8_0_31);
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }

    @Test
    void mySql5Test() {
        initializeDatabase(mySqlContainer5_7_40);
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }
}

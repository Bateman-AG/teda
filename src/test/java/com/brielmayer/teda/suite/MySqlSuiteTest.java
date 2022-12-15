package com.brielmayer.teda.suite;

import com.brielmayer.teda.TestExecutor;
import com.brielmayer.teda.api.TedaSuite;
import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.handler.LoadHandlerTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class MySqlSuiteTest {

    @Container
    public static MySQLContainer mySqlContainer8_0_31 = new MySQLContainer<>("mysql:8.0.31");
    @Container
    public static MySQLContainer mySqlContainer5_7_40 = new MySQLContainer<>("mysql:5.7.40");
    Database database;

    void initializeDatabase(MySQLContainer container) {
        String jdbcUrl = container.getJdbcUrl();
        String user = container.getUsername();
        String password = container.getPassword();

        DatabaseConnection connection = new DatabaseConnection(jdbcUrl, user, password);
        database = DatabaseCreator.createDatabase(connection);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");
    }

    @Test
    void mySql8Test() {
        initializeDatabase(mySqlContainer8_0_31);
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database);
    }

    @Test
    void mySql5Test() {
        initializeDatabase(mySqlContainer5_7_40);
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database);
    }
}

package com.brielmayer.teda.suite;

import com.brielmayer.teda.TestExecutor;
import com.brielmayer.teda.api.TedaSuite;
import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.handler.LoadHandlerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class InputOutputDatabasesTest {

    @Container
    public static MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.31");
    @Container
    public static MySQLContainer mySqlContainer2 = new MySQLContainer<>("mysql:8.0.31")
            .withUsername("TestUser2")
            .withPassword("TestSecret2");
    Database database;
    Database database2;

    @BeforeEach
    void setup() {
        //setup first DB
        DatabaseConnection connection = getDatabaseConnection(mySqlContainer);
        database = DatabaseCreator.createDatabase(connection);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup second DB
        DatabaseConnection connection2 = getDatabaseConnection(mySqlContainer2);
        database2 = DatabaseCreator.createDatabase(connection2);
        database2.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");
    }

    @Test
    void suiteTestContainer() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/DIFFERENT_DB_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database, database2);
    }

    private DatabaseConnection getDatabaseConnection(MySQLContainer container) {
        String jdbcUrl = container.getJdbcUrl();
        String user = container.getUsername();
        String password = container.getPassword();

        return new DatabaseConnection(jdbcUrl, user, password);
    }
}

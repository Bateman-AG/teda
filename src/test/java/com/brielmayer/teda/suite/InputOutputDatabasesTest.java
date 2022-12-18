package com.brielmayer.teda.suite;

import com.brielmayer.teda.TestExecutor;
import com.brielmayer.teda.api.TedaSuite;
import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.handler.LoadHandlerTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InputOutputDatabasesTest {

    @Container
    public static MySQLContainer mySqlContainer = new MySQLContainer<>("mysql:8.0.31");
    @Container
    public static MySQLContainer mySqlContainer2 = new MySQLContainer<>("mysql:8.0.31")
            .withUsername("TestUser2")
            .withPassword("TestSecret2");
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1");
    Database database;
    Database database2;
    Database database3;

    @BeforeAll
    void setup() {
        //setup first DB
        DatabaseConnection connection = getDatabaseConnection(mySqlContainer);
        database = DatabaseCreator.createDatabase(connection);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup second DB
        DatabaseConnection connection2 = getDatabaseConnection(mySqlContainer2);
        database2 = DatabaseCreator.createDatabase(connection2);
        database2.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup third DB
        DatabaseConnection connection3 = getDatabaseConnection(postgreSQLContainer);
        database3 = DatabaseCreator.createDatabase(connection3);
        database3.executeQuery("CREATE TABLE STUDENT (id text, name text, age int4, average float8)");
    }

    @Test
    void testWithTwoDatabases() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/DIFFERENT_DB_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database, database2);
    }

    @Test
    void testWithTwoDifferentDatabaseTypes() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/DIFFERENT_DB_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database, database3);
    }

    private DatabaseConnection getDatabaseConnection(JdbcDatabaseContainer container) {
        String jdbcUrl = container.getJdbcUrl();
        String user = container.getUsername();
        String password = container.getPassword();

        return new DatabaseConnection(jdbcUrl, user, password);
    }
}

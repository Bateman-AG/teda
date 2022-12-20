package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.handler.LoadHandlerTest;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.ds.PGSimpleDataSource;
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
    public static MySQLContainer<?> mySqlContainer = new MySQLContainer<>("mysql:8.0.31");

    @Container
    public static MySQLContainer<?> mySqlContainer2 = new MySQLContainer<>("mysql:8.0.31")
            .withUsername("TestUser2")
            .withPassword("TestSecret2");
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1");

    private BaseDatabase database;
    private BaseDatabase database2;
    private BaseDatabase database3;

    @BeforeAll
    void setup() {
        //setup first DB
        DatabaseConnection connection = getDatabaseConnection(mySqlContainer);
        MysqlDataSource db = new MysqlDataSource();
        db.setPassword(connection.getPassword());
        db.setUrl(connection.getUrl());
        db.setUser(connection.getUser());
        database = DatabaseFactory.createDatabase(db);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup second DB
        DatabaseConnection connection2 = getDatabaseConnection(mySqlContainer2);
        MysqlDataSource db2 = new MysqlDataSource();
        db2.setPassword(connection2.getPassword());
        db2.setUrl(connection2.getUrl());
        db2.setUser(connection2.getUser());
        database2 = DatabaseFactory.createDatabase(db2);
        database2.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup third DB
        DatabaseConnection connection3 = getDatabaseConnection(postgreSQLContainer);
        PGSimpleDataSource db3 = new PGSimpleDataSource();
        db3.setPassword(connection3.getPassword());
        db3.setUrl(connection3.getUrl());
        db3.setUser(connection3.getUser());
        database3 = DatabaseFactory.createDatabase(db3);
        database3.executeQuery("CREATE TABLE STUDENT (id text, name text, age int4, average float8)");
    }

    @Test
    void testWithTwoDatabases() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/DIFFERENT_DB_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), database2.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }

    @Test
    void testWithTwoDifferentDatabaseTypes() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/DIFFERENT_DB_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), database3.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }

    private DatabaseConnection getDatabaseConnection(JdbcDatabaseContainer<?> container) {
        String jdbcUrl = container.getJdbcUrl();
        String user = container.getUsername();
        String password = container.getPassword();

        return new DatabaseConnection(jdbcUrl, user, password);
    }
}

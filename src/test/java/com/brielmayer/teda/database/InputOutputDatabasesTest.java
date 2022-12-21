package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
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
        database = DatabaseFactory.createDatabase(getMySqlDataSource(mySqlContainer));
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup second DB
        database2 = DatabaseFactory.createDatabase(getMySqlDataSource(mySqlContainer2));
        database2.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");

        // setup third DB
        database3 = DatabaseFactory.createDatabase(getPostgreDataSource(postgreSQLContainer));
        database3.executeQuery("CREATE TABLE STUDENT (id text, name text, age int4, average float8)");
    }

    @Test
    void testWithTwoDatabases() {
        new TedaSuite(database.getDataSource(), database2.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/DIFFERENT_DB_TEST.xlsx");
    }

    @Test
    void testWithTwoDifferentDatabaseTypes() {
        new TedaSuite(database.getDataSource(), database3.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/DIFFERENT_DB_TEST.xlsx");
    }

    private MysqlDataSource getMySqlDataSource(JdbcDatabaseContainer<?> container) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setPassword(container.getPassword());
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        return dataSource;
    }

    private PGSimpleDataSource getPostgreDataSource(JdbcDatabaseContainer<?> container) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setPassword(container.getPassword());
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        return dataSource;
    }
}

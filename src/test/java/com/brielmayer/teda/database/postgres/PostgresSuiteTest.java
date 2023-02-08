package com.brielmayer.teda.database.postgres;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.util.ResourceReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresSuiteTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1");

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        // Setup PostgreSQL database
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(postgreSQLContainer.getJdbcUrl());
        dataSource.setUser(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());

        // Create and initialize database
        database = DatabaseFactory.createDatabase(dataSource);
        database.executeQuery(ResourceReader.getResourceAsString("database/postgres/CREATE_TEST_TABLE.sql"));
    }

    @Test
    void loadTest() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet(ResourceReader.getResourceAsInputStream("teda/LOAD_TEST.xlsx"));
    }
}

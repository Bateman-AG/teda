package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import oracle.jdbc.datasource.impl.OracleDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers
public class OracleSuiteTest {

    @Container
    public static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withDatabaseName("test")
            .withUsername("testUser")
            .withPassword("testPassword");
    private BaseDatabase database;

    @BeforeEach
    void setup() {
        try {
            OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(oracleContainer.getJdbcUrl());
            dataSource.setUser(oracleContainer.getUsername());
            dataSource.setPassword(oracleContainer.getPassword());
            database = DatabaseFactory.createDatabase(dataSource);
            database.executeQuery("CREATE TABLE STUDENT (id varchar(255), name varchar(255), age number(3), average number)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void suiteTestContainer() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }
}

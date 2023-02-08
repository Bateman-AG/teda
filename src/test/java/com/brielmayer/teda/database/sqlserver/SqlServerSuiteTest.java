package com.brielmayer.teda.database.sqlserver;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.util.ResourceReader;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class SqlServerSuiteTest {

    @Container
    public static MSSQLServerContainer<?> mssqlServerContainer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2017-CU12")
            .acceptLicense();

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        // Setup SQL Server database
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(mssqlServerContainer.getJdbcUrl());
        dataSource.setUser(mssqlServerContainer.getUsername());
        dataSource.setPassword(mssqlServerContainer.getPassword());

        // Create and initialize database
        database = DatabaseFactory.createDatabase(dataSource);
        database.executeQuery(ResourceReader.getResourceAsString("database/sqlserver/CREATE_TEST_TABLE.sql"));
    }

    @Test
    void loadTest() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet(ResourceReader.getResourceAsInputStream("teda/LOAD_TEST.xlsx"));
    }
}
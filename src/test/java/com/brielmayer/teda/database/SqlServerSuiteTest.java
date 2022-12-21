package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class SqlServerSuiteTest {

    @Container
    public static MSSQLServerContainer<?> msSqlContainer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2017-CU12")
            .acceptLicense();

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(msSqlContainer.getJdbcUrl());
        dataSource.setUser(msSqlContainer.getUsername());
        dataSource.setPassword(msSqlContainer.getPassword());
        database = DatabaseFactory.createDatabase(dataSource);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average decimal(18,9))");
    }

    @Test
    void suiteTestContainer() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }
}

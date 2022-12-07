package com.brielmayer.teda.suite;

import com.brielmayer.teda.TestExecutor;
import com.brielmayer.teda.api.TedaSuite;
import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.handler.LoadHandlerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class SqlServerSuiteTest {

    @Container
    public static MSSQLServerContainer mySqlContainer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2017-CU12")
            .acceptLicense();
    Database database;

    @BeforeEach
    void setup() {
        String jdbcUrl = mySqlContainer.getJdbcUrl();
        String user = mySqlContainer.getUsername();
        String password = mySqlContainer.getPassword();

        DatabaseConnection connection = new DatabaseConnection(jdbcUrl, user, password);
        database = DatabaseCreator.createDatabase(connection);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average decimal(18,9))");
    }

    @Test
    void suiteTestContainer() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(new TestExecutor());
        tedaSuite.executeSheet(inputStream, database);
    }
}

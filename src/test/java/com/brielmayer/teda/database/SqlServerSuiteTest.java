package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.handler.LoadHandlerTest;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class SqlServerSuiteTest {

    @Container
    public static MSSQLServerContainer<?> mySqlContainer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2017-CU12")
            .acceptLicense();

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        String jdbcUrl = mySqlContainer.getJdbcUrl();
        String user = mySqlContainer.getUsername();
        String password = mySqlContainer.getPassword();

        SQLServerDataSource db = new SQLServerDataSource();
        db.setPassword(password);
        db.setURL(jdbcUrl);
        db.setUser(user);
        database = DatabaseFactory.createDatabase(db);
        database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average decimal(18,9))");
    }

    @Test
    void suiteTestContainer() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), (new LogExecutionHandler()));
        tedaSuite.executeSheet(inputStream);
    }
}

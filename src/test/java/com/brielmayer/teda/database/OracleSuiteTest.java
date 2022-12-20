package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.handler.LoadHandlerTest;
import oracle.jdbc.datasource.impl.OracleDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
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
        String jdbcUrl = oracleContainer.getJdbcUrl();
        String user = oracleContainer.getUsername();
        String password = oracleContainer.getPassword();

        try {
            OracleDataSource db = new OracleDataSource();
            db.setPassword(password);
            db.setURL(jdbcUrl);
            db.setUser(user);
            database = DatabaseFactory.createDatabase(db);
            database.executeQuery("CREATE TABLE STUDENT (id varchar(255), name varchar(255), age number(3), average number)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void suiteTestContainer() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }
}

package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import com.brielmayer.teda.handler.LoadHandlerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;

@Testcontainers
public class PostgresSuiteTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1");

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String user = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        PGSimpleDataSource db = new PGSimpleDataSource();
        db.setPassword(password);
        db.setURL(jdbcUrl);
        db.setUser(user);
        database = DatabaseFactory.createDatabase(db);
        database.executeQuery("CREATE TABLE STUDENT (id text, name text, age int4, average float8)");
    }

    @Test
    void suiteTestContainer() {
        InputStream inputStream = LoadHandlerTest.class.getClassLoader()
                .getResourceAsStream("teda/LOAD_TEST.xlsx");
        TedaSuite tedaSuite = new TedaSuite(database.getDataSource(), new LogExecutionHandler());
        tedaSuite.executeSheet(inputStream);
    }
}

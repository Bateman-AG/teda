package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import com.brielmayer.teda.database.BaseDatabase;
import com.brielmayer.teda.database.DatabaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers
public class MariaSuiteTest {

    @Container
    public static MariaDBContainer<?> mariaDbContainer = new MariaDBContainer<>("mariadb:10.9.4");

    private BaseDatabase database;

    @BeforeEach
    void initializeDatabase() {
        try {
            MariaDbDataSource db = new MariaDbDataSource(mariaDbContainer.getJdbcUrl());
            db.setUser(mariaDbContainer.getUsername());
            db.setPassword(mariaDbContainer.getPassword());

            database = DatabaseFactory.createDatabase(db);
            database.executeQuery("CREATE TABLE STUDENT (id int, name varchar(255), age int, average double)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void mariaDbTest() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }
}

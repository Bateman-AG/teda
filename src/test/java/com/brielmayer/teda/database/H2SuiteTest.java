package com.brielmayer.teda.database;

import com.brielmayer.teda.LogExecutionHandler;
import com.brielmayer.teda.TedaSuite;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class H2SuiteTest {

    private BaseDatabase database;

    @BeforeEach
    void setup() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:~/test");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        database = DatabaseFactory.createDatabase(dataSource);
        database.executeQuery("CREATE TABLE IF NOT EXISTS STUDENT (id int, name varchar(255), age int, average decimal(18,9))");
    }

    @Test
    void suiteTestContainer() {
        new TedaSuite(database.getDataSource(), new LogExecutionHandler())
                .executeSheet("src/test/resources/teda/LOAD_TEST.xlsx");
    }
}

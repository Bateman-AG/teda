package com.brielmayer.teda.database;

import com.brielmayer.teda.database.mysql.MySqlDatabase;
import com.brielmayer.teda.database.postgres.PostgresDatabase;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseFactoryTest {

    @Test
    public void mySqlDataSource_createsDatabase() {
        MysqlDataSource db = new MysqlDataSource();
        BaseDatabase database = DatabaseFactory.createDatabase(db);
        assertTrue(database instanceof MySqlDatabase);
    }

    @Test
    public void postgresDataSource_createsDatabase() {
        PGSimpleDataSource db = new PGSimpleDataSource();
        BaseDatabase database = DatabaseFactory.createDatabase(db);
        assertTrue(database instanceof PostgresDatabase);
    }

}

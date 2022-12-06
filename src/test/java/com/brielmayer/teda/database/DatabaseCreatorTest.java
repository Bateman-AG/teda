package com.brielmayer.teda.database;

import com.brielmayer.teda.database.type.DatabaseCreator;
import com.brielmayer.teda.exception.TedaException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseCreatorTest {

    @Test
    public void validMySqlJdbcString_createsDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:mysql://localhost:8081/", "", "");
        Database database = DatabaseCreator.createDatabase(databaseConnection);
        assertNotNull(database);
    }

    @Test
    public void validPostgresJdbcString_createsDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:8081/", "", "");
        Database database = DatabaseCreator.createDatabase(databaseConnection);
        assertNotNull(database);
    }

    @Test
    public void unknownJdbcString_throwsTeDaException() {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:teda://localhost:8081/", "", "");
        assertThrows(TedaException.class, () -> DatabaseCreator.createDatabase(databaseConnection));
    }

}

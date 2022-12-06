package com.brielmayer.teda.database.type;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.database.DatabaseConnection;
import com.brielmayer.teda.exception.TedaException;

import java.util.ServiceLoader;

public class DatabaseCreator {

    public static Database createDatabase(DatabaseConnection databaseConnection) throws TedaException {
        ServiceLoader<DatabaseType> loader = ServiceLoader.load(DatabaseType.class);
        loader.reload();
        for (DatabaseType implClass : loader) {
            if (implClass.handlesJDBCUrl(databaseConnection.getUrl())) {
                return implClass.createDatabase(databaseConnection);
            }
        }
        throw new TedaException("No supported database type found");
    }
}

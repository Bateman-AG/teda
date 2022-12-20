package com.brielmayer.teda.database;

import com.brielmayer.teda.exception.TedaException;

import javax.sql.DataSource;
import java.util.ServiceLoader;

public class DatabaseFactory {

    public static BaseDatabase createDatabase(DataSource dataSource) throws TedaException {
        ServiceLoader<DatabaseType> loader = ServiceLoader.load(DatabaseType.class);
        loader.reload();
        for (DatabaseType implClass : loader) {
            if (implClass.handlesDataSource(dataSource)) {
                return implClass.createDatabase(dataSource);
            }
        }

        throw new TedaException("No supported database type found");
    }
}

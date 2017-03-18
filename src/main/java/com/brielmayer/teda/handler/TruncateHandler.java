package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.Database;
import com.brielmayer.teda.exception.TedaException;

import java.sql.SQLException;

public class TruncateHandler {
    public static void truncate(Database database, String tableName) {
        database.truncateTable(tableName);
    }
}

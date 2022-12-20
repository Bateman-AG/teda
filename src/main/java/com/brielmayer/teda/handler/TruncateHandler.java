package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.BaseDatabase;

public class TruncateHandler {
    public static void truncate(BaseDatabase database, String tableName) {
        database.truncateTable(tableName);
    }
}

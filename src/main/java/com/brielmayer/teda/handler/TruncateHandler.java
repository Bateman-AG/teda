package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.BaseDatabase;

public final class TruncateHandler {

    public static void truncate(final BaseDatabase database, final String tableName) {
        database.truncateTable(tableName);
    }
}

package com.brielmayer.teda.handler;

import com.brielmayer.teda.database.BaseDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TruncateHandler {

    private static final Logger log  = LoggerFactory.getLogger(TruncateHandler.class);

    public static void truncate(final BaseDatabase database, final String tableName) {
        log.info("Truncate table: {}", tableName);
        database.truncateTable(tableName);
    }
}

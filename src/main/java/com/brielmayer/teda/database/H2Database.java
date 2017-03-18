package com.brielmayer.teda.database;

import com.brielmayer.teda.model.Header;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class H2Database extends Database {

    public H2Database(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void truncateTable(String tableName) {

    }

    @Override
    public void dropTable(String tableName) {

    }

    @Override
    public void insertRow(String tableName, Map<String, Object> row) {

    }

    @Override
    public List<Map<String, Object>> queryForList(String tableName, List<Header> headers) {
        return null;
    }
}

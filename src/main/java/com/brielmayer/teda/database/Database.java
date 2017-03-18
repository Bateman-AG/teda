package com.brielmayer.teda.database;

import com.brielmayer.teda.exception.TedaException;
import com.brielmayer.teda.model.Header;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Database {

        DataSource dataSource;

        public Database(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public int executeQuery(String query) {
            return getJdbcTemplate().update(query);
        }

        public int executeQuery(String query, String... params) {
            return executeQuery(String.format(query, params));
        }

        public JdbcTemplate getJdbcTemplate() {
            return new JdbcTemplate(dataSource);
        }

        public abstract void truncateTable(String tableName);
        public abstract void dropTable(String tableName);
        public abstract void insertRow(String tableName, Map<String, Object> row) throws SQLException;
        public abstract List<Map<String, Object>> queryForList(String tableName, List<Header> headers);
}

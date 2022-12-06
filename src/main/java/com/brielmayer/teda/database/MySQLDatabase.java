package com.brielmayer.teda.database;

import com.brielmayer.teda.model.Header;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MySQLDatabase extends Database {

    public MySQLDatabase(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void truncateTable(String tableName) {
        executeQuery("TRUNCATE TABLE `%s`;", tableName);
    }

    @Override
    public void dropTable(String tableName) {
        executeQuery("DROP TABLE IF EXISTS `%s`;", tableName);
    }

    @Override
    public void insertRow(String tableName, Map<String, Object> row) throws SQLException {
        String query = "INSERT INTO %s(%s) VALUES (%s)";

        // "?" * header size
        List<String> placeHolder = Collections.nCopies(row.keySet().size(), "?");
        query = String.format(query, tableName, String.join(",", row.keySet()), String.join(",", placeHolder));

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int parameterIndex = 1;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    preparedStatement.setObject(parameterIndex++, entry.getValue());
                }
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String tableName, List<Header> headers) {
        String query = "SELECT %s FROM %s";
        query = String.format(query, String.join(",", headers.stream().map(Header::getName).collect(Collectors.toList())), tableName);
        return getJdbcTemplate().queryForList(query);
    }
}

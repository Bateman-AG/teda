package com.brielmayer.teda.database;

import com.brielmayer.teda.map.LinkedCaseInsensitiveMap;
import com.brielmayer.teda.model.Header;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseDatabase {

    static class ResultSetMapper {

        public static Map<String, Object> mapResultSet(ResultSet resultSet) throws SQLException {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            Map<String, Object> mapOfColValues = createColumnMap(columnCount);

            for (int i = 1; i <= columnCount; ++i) {
                String key = getColumnKey(resultSetMetaData, i);
                Object obj = getColumnValue(resultSet, i);
                mapOfColValues.put(key, obj);
            }

            return mapOfColValues;
        }

        protected static Map<String, Object> createColumnMap(int columnCount) {
            return new LinkedCaseInsensitiveMap<>(columnCount);
        }

        protected static String getColumnKey(ResultSetMetaData resultSetMetaData, int index) throws SQLException {
            String name = resultSetMetaData.getColumnLabel(index);
            if (name == null || name.length() < 1) {
                name = resultSetMetaData.getColumnName(index);
            }

            return name;
        }

        protected static Object getColumnValue(ResultSet rs, int index) throws SQLException {
            return rs.getObject(index);
        }
    }

    protected DataSource dataSource;

    public BaseDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeQuery(String query) {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO better exception handling
        }
    }

    protected int executeQuery(String query, String... params) {
        return executeQuery(String.format(query, params));
    }

    protected List<Map<String, Object>> queryForList(String query) {
        List<Map<String, Object>> result = new ArrayList<>();
        try (Statement statement = dataSource.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.add(ResultSetMapper.mapResultSet(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e); // TODO better exception handling
        }
    }

    protected void executeRow(String query, Map<String, Object> row) throws SQLException {
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

    public DataSource getDataSource() {
        return dataSource;
    }

    public abstract void truncateTable(String tableName);

    public abstract void dropTable(String tableName);

    public abstract void insertRow(String tableName, Map<String, Object> row) throws SQLException;

    public abstract List<Map<String, Object>> select(String tableName, List<Header> headers);
}

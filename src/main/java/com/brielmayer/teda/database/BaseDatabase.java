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

    static final class ResultSetMapper {

        public static Map<String, Object> mapResultSet(final ResultSet resultSet) throws SQLException {
            final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            final int columnCount = resultSetMetaData.getColumnCount();
            final Map<String, Object> mapOfColValues = createColumnMap(columnCount);

            for (int i = 1; i <= columnCount; ++i) {
                final String key = getColumnKey(resultSetMetaData, i);
                final Object obj = getColumnValue(resultSet, i);
                mapOfColValues.put(key, obj);
            }

            return mapOfColValues;
        }

        private static Map<String, Object> createColumnMap(final int columnCount) {
            return new LinkedCaseInsensitiveMap<>(columnCount);
        }

        private static String getColumnKey(final ResultSetMetaData resultSetMetaData, final int index) throws SQLException {
            String name = resultSetMetaData.getColumnLabel(index);
            if (name == null || name.length() < 1) {
                name = resultSetMetaData.getColumnName(index);
            }

            return name;
        }

        private static Object getColumnValue(final ResultSet rs, final int index) throws SQLException {
            return rs.getObject(index);
        }
    }

    protected DataSource dataSource;

    public BaseDatabase(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected int executeQuery(final String query) {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            return statement.executeUpdate(query);
        } catch (final SQLException e) {
            throw new RuntimeException(e); // TODO better exception handling
        }
    }

    protected int executeQuery(final String query, final String... params) {
        return executeQuery(String.format(query, params));
    }

    protected List<Map<String, Object>> queryForList(final String query) {
        final List<Map<String, Object>> result = new ArrayList<>();
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                result.add(ResultSetMapper.mapResultSet(resultSet));
            }
            return result;
        } catch (final SQLException e) {
            throw new RuntimeException(e); // TODO better exception handling
        }
    }

    protected void executeRow(final String query, final Map<String, Object> row) throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int parameterIndex = 1;
                for (final Map.Entry<String, Object> entry : row.entrySet()) {
                    preparedStatement.setObject(parameterIndex, entry.getValue());
                    parameterIndex++;
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

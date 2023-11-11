package edu.unm;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

/**
 * Static method utility class for supporting DAO operations.
 */
public interface DAOUtils {

    String JDBC_CONNECTION_STRING = "jdbc:sqlite:voters.db";

    static Logger getLogger() {
        return Logger.getLogger(DAOUtils.class.getName());
    }

    /**
     * Retrive a SQLite database {@link Connection} object. This would normally
     * be done in a context safe way, however for project simplicity we have
     * simplified the process.
     *
     * @return the database connection
     */
    static Connection getConnection() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(JDBC_CONNECTION_STRING);

            getLogger().log(INFO, "[SQLStats] Connection to SQLite database " +
                    "was successful.");
        } catch (SQLException e) {
            getLogger().log(WARNING, "[SQLStats] Connection to SQLite database" +
                            " was unsuccessful. {0}",
                    e.getMessage().trim());
        }

        return conn;
    }

    /**
     * Null-safe way to convert a {@link LocalDateTime} value to a {@link Timestamp}
     *
     * @param timestamp the LocalDateTime to convert to a java.sql.Timestamp
     * @return the java.sql.Timestamp, or null if the LocalDateTime was given as null
     */
    static Timestamp asSqlTimestamp(LocalDateTime timestamp) {
        return timestamp != null ? Timestamp.valueOf(timestamp) : null;
    }

    /**
     * Null-safe way to convert a SQL {@code DATE} or {@code DATETIME} type from
     * a {@link ResultSet}, and return it as a {@link LocalDateTime}.
     *
     * @param rs            the ResultSet containing the row data
     * @param columnIndex   the label of the column containing the DATE or DATETIME value
     * @return              the column data as a LocalDateTime, or null if the column
     *                      contains no data
     * @throws SQLException when ResultSet.getTimestamp() fails
     */
    static LocalDateTime getLocalDateTime(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnIndex);
        return ts != null ? ts.toLocalDateTime() : null;
    }

    /**
     * Executes a database query from a prepared statement, performing an action
     * on each row of the result.
     *
     * @param ps            the prepared statement to be executed
     * @param setter        the functional interface providing parameters to the query
     * @param action        the functional interface consuming a row from the ResultSet
     * @return the number of rows returned by the query, i.e., how often the action was called
     * @throws SQLException if the database query could not be executed
     */
    static int queryRows(
            PreparedStatement ps,
            ParameterSupplier setter,
            ResultSetConsumer action) throws SQLException {
        if (setter != null) setter.setParameters(ps);

        try (ResultSet rs = ps.executeQuery()) {
            int rows = 0;

            while (rs.next()) {
                action.consume(rs);
                ++rows;
            }

            return rows;
        }
    }

    /**
     * Executes a database query, performing an action on each row of the result.
     *
     * @param conn          the database connection
     * @param query         the SQL query to be executed
     * @param setter        the functional interface providing parameters to the query
     * @param action        the functional interface consuming a row from the ResultSet
     * @return              the number of rows returned by the query, i.e., how often action was called
     * @throws SQLException if the database query could not be executed
     */
    static int queryRows(
            Connection conn,
            String query,
            ParameterSupplier setter,
            ResultSetConsumer action) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            return queryRows(ps, setter, action);
        }
    }

    /**
     * Executes a database query from the prepared statement for a list of objects.
     * This is a more specialized and parametrized version of the generic {@code queryRows()}
     * method.
     *
     * @param ps     the prepared statment to be executed
     * @param setter the functional interface providing parameters to the query
     * @param mapper the functional interface mapping columns from the ResultSet
     *               to the returned object type
     * @param <E>    the object type to be returned by this query
     * @return       a list of objects of type E, one element for each row in the
     *               ResultSet
     * @throws SQLException if the database query could not be executed
     */
    static <E> List<E> queryForList(
            PreparedStatement ps,
            ParameterSupplier setter,
            ResultSetMapper<E> mapper) throws SQLException {
        List<E> result = new ArrayList<>();

        queryRows(ps, setter, rs -> result.add(mapper.mapResult(rs)));

        return result;
    }

    /**
     * Executes a database query from the prepared statement for a list of objects.
     * This is a more specialized and parametrized version of the generic {@code queryRows()}
     * method.
     *
     * @param conn   the database connection
     * @param query  the SQL query to be executed
     * @param setter the functional interface providing parameters to the query
     * @param mapper the functional interface mapping columns from the ResultSet
     *               to the returned object type
     * @param <E>    the object type to be returned by this query
     * @return       a list of objects of type E, one element for each row in the
     *               ResultSet
     * @throws SQLException if the database query could not be executed
     */
    static <E> List<E> queryForList(
            Connection conn,
            String query,
            ParameterSupplier setter,
            ResultSetMapper<E> mapper) throws SQLException {
        List<E> result = new ArrayList<>();

        queryRows(conn, query, setter, rs -> result.add(mapper.mapResult(rs)));

        return result;
    }

    @FunctionalInterface
    interface ParameterSupplier {
        void setParameters(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    interface ResultSetConsumer {
        void consume(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    interface ResultSetMapper<T> {
        T mapResult(ResultSet rs) throws SQLException;
    }

}

package edu.unm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;

/**
 * A base class from which all SQLite specific Data Access Objects can be derived.
 * Common functionality surrounding DAO lifecycle and dependencies should be
 * defined in this abstract class.
 * <p>
 * The only dependency that DAO classes need is a database connection. For now,
 * connections are <strong>NOT</strong> being released when the DAO is closed.
 */
public abstract class AbstractSQLiteDAO implements BaseDAO {

    private final Connection conn;

    protected AbstractSQLiteDAO(Connection conn) {
        this.conn = conn;
    }

    private static Logger getLogger() {
        return Logger.getLogger(AbstractSQLiteDAO.class.getName());
    }

    public Connection getConn() {
        return conn;
    }

    @Override
    public void commit() {
        try {
            getConn().commit();
        } catch (SQLException e) {
            getLogger().log(WARNING, "[SQLStats] COMMIT failed: {0}",
                    e.getMessage().trim());
        }
    }

    @Override
    public void rollback() {
        try {
            getConn().rollback();
        } catch (SQLException e) {
            getLogger().log(WARNING, "[SQLStats] ROLLBACK failed: {0}",
                    e.getMessage().trim());
        }
    }
}

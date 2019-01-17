package persistence.database;

import common.LoggerLoader;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Wrapper class for connection and transaction management.
 * Implements {@code Closeable}
 */
public class DbContext implements Closeable {
    private static final String LOG_TRANSACTION_BEGIN = "Transaction begin.";
    private static final String LOG_TRANSACTION_COMMITTED = "Transaction committed.";
    private static final String LOG_TRANSACTION_ROLLED_BACK = "Transaction rolled back.";
    private static final String LOG_EXCEPTION_IN_TRANSACTION_ENDING = "Exception in transaction ending!";
    private static final Logger LOGGER = LoggerLoader.getLogger(DbContext.class);
    private Connection connection;
    private boolean autoCommit = true;

    public DbContext() { }

    /**
     * Generic static method, executes {@code body} in a transaction with new connection. Than commits transaction.
     *
     * @param body instance of {@code TransactionBody} to invoke.
     * @param <R>  type of result
     * @return result of body.invoke() or {@code null} if transaction not ended successfully or exception happen.
     */
    public static <R> R executeTransactionOrNull(TransactionBody<R> body) {
        R result;
        try (DbContext dbContext = new DbContext()) {
            dbContext.beginTransaction();
            result = body.invoke(dbContext);
            return dbContext.endTransaction(true) ? result : null;
        } catch (DAOException | SQLException e) {
            LOGGER.error(e);
        }
        return null;
    }

    /**
     * Generic static method, executes {@code body} with a given transaction context.
     *
     * @param body    instance of {@code TransactionBody} to invoke.
     * @param <R>     type of result
     * @param context DI of current connection and transaction context
     * @return result of body.invoke() or {@code null} if exception
     */
    public static <R> R executeOrNull(TransactionBody<R> body, DbContext context) {
        try {
            return body.invoke(context);
        } catch (DAOException | SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }

    /**
     * Returns new connection from connection pool
     *
     * @return a new connection
     * @throws DAOException if any {@code SQLException} happen.
     */
    public Connection getConnection() throws DAOException {
        try {
            if (connection == null || this.connection.isClosed()) {
                connection = MySQLConnectionPool.getConnection();
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        }
        return connection;
    }

    /**
     * Close connection.
     * If there is started transaction - rolling it back.
     */
    @Override
    public void close() {
        try {
            if (!autoCommit) {
                endTransaction(false);
            }
            MySQLConnectionPool.closeConnection(connection);
        } catch (DAOException e) {
            LOGGER.error(e);
        }
        connection = null;
    }

    /**
     * Just set flag {@code autoCommit} to {@code false}
     */
    public void beginTransaction() {
        autoCommit = false;
        LOGGER.info(LOG_TRANSACTION_BEGIN);
    }

    /**
     * Ends transaction.
     *
     * @param doCommit If this flag is true - transaction committed, otherwise - rolled  back.
     * @return {@code true} if no exceptions
     */
    public boolean endTransaction(boolean doCommit) {
        autoCommit = true;
        try {
            if (doCommit) {
                commit();
                LOGGER.info(LOG_TRANSACTION_COMMITTED);
            } else {
                rollback();
                LOGGER.info(LOG_TRANSACTION_ROLLED_BACK);
            }
            return true;
        } catch (DAOException e) {
            LOGGER.error(LOG_EXCEPTION_IN_TRANSACTION_ENDING, e);
            return false;
        }
    }

    /**
     * Committing transaction or roll it back if exception happen
     *
     * @throws DAOException if any {@code SQLException} happen.
     */
    private void commit() throws DAOException {
        try {
            if (connection != null && !this.connection.isClosed()) {
                connection.commit();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            rollback();
        }
    }

    /**
     * Rollback transaction
     *
     * @throws DAOException if any {@code SQLException} happen.
     */
    private void rollback() throws DAOException {
        try {
            if (connection != null && !this.connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        }
    }

    /**
     * Functional interface for executing in transaction.
     *
     * @param <R> Type of result of method invoke()
     */
    public interface TransactionBody<R> {
        R invoke(DbContext context) throws DAOException, SQLException;
    }
}
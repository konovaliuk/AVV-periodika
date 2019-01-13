package persistence.database;

import common.LoggerLoader;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public class DBContext implements Closeable {
    private static final Logger LOGGER = LoggerLoader.getLogger(DBContext.class);
    private Connection connection;
    private boolean autoCommit = true;

    public DBContext() { }

    public static <R> R executeTransactionOrNull(TransactionBody<R> body) {
        R result;
        try (DBContext dbContext = new DBContext()) {
            dbContext.beginTransaction();
            result = body.invoke(dbContext);
            return dbContext.endTransaction(true) ? result : null;
        } catch (DAOException | SQLException e) {
            LOGGER.error(e);
        }
        return null;
    }

    public static <R> R executeOrNull(TransactionBody<R> body, DBContext context) {
        try {
            return body.invoke(context);
        } catch (DAOException | SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Connection getConnection() throws DAOException {
        try {
            if (connection == null || this.connection.isClosed()) {
                connection = MySQLConnectionPool.getConnection();
                connection.setAutoCommit(autoCommit);
                LOGGER.info("Transaction started.");
            }
        } catch (SQLException e) {
            //todo add logger
            throw new DAOException(e);
        }
        return connection;
    }

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

    public void beginTransaction() {
        autoCommit = false;
    }

    public boolean endTransaction(boolean doCommit) {
        autoCommit = true;
        try {
            if (doCommit) {
                commit();
                LOGGER.info("Transaction committed.");
            } else {
                rollback();
                LOGGER.info("Transaction rolled back.");
            }
            return true;
        } catch (DAOException e) {
            LOGGER.error("Exception in transaction ending!", e);
            return false;
        }
    }

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

    public interface TransactionBody<R> {
        R invoke(DBContext context) throws DAOException, SQLException;
    }
}
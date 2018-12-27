package persistence.database;

import common.LoggerLoader;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;

import java.sql.Connection;
import java.sql.SQLException;

public class DBContext {
    private static final Logger LOGGER = LoggerLoader.getLogger(DBContext.class);
    private Connection connection;
    private boolean autoCommit;

    public DBContext(boolean startTransaction) {
        if (startTransaction) {
            beginTransaction();
        }
    }

    public static <R> R executeTransactionOrNull(TransactionBody<R> body) {
        DBContext dbContext = new DBContext(true);
        R result;
        try {
            result = body.invoke(dbContext);
        } catch (DAOException | SQLException e) {
            LOGGER.error(e);
            dbContext.endTransaction(false);
            return null;
        }
        return dbContext.endTransaction(true) ? result : null;
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
            //todo add logger
            rollback();
        } finally {
            //todo double call if rollback was executed
            MySQLConnectionPool.closeConnection(connection);
            connection = null;
        }
    }

    private void rollback() throws DAOException {
        try {
            if (connection != null && !this.connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            //todo add logger
            throw new DAOException(e);
        } finally {
            MySQLConnectionPool.closeConnection(connection);
            connection = null;
        }
    }

    public interface TransactionBody<R> {
        R invoke(DBContext context) throws SQLException, DAOException;
    }
}
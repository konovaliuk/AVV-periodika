package persistence.database;

import common.LoggerLoader;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import static common.ResourceManager.DB_POOL_DATASOURCE_NAME;
import static common.ResourceManager.RM_DATABASE;

public final class MySQLConnectionPool {
    private static final Logger LOGGER = LoggerLoader.getLogger(persistence.database.MySQLConnectionPool.class);
    private static persistence.database.MySQLConnectionPool instance = null;
    private volatile static boolean instanceCreated = false;
    private static ReentrantLock lock = new ReentrantLock();
    private DataSource dataSource;

    private MySQLConnectionPool() throws DAOException {
        try {
            dataSource = (DataSource) new InitialContext().lookup(RM_DATABASE.get(DB_POOL_DATASOURCE_NAME));
        } catch (NamingException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        }
    }

    public static Connection getConnection() throws DAOException {
        try {
            return getInstance().dataSource.getConnection();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public static void closeConnection(Connection conn) throws DAOException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DAOException(e);
        }
    }

    private static persistence.database.MySQLConnectionPool getInstance() throws DAOException {
        if (!instanceCreated) {
            lock.lock();
            try {
                if (!instanceCreated) {
                    instance = new persistence.database.MySQLConnectionPool();
                    instanceCreated = true;
                }
            } catch (DAOException e) {
                throw new DAOException(e);
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

}
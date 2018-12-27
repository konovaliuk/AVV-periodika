package persistence.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized;
import persistence.dao.DAOException;
import persistence.dao.GenericReadonlyDAO;
import persistence.dao.DAOFactory;
import model.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;


public class MySqlDaoTest extends GenericDAOTest<Connection> {

    private static final DAOFactory factory = new DAOFactory();
    private Connection connection;
    private GenericReadonlyDAO dao;

    public MySqlDaoTest(Class clazz, Entity<Long> notPersistedDto) {
        super(clazz, notPersistedDto);
    }

    @Parameterized.Parameters
    public static Collection getParameters() {
        return Arrays.asList(
//                new Object[][]{
//                {Group.class, new Group()},
//                {Student.class, new Student()}
//        }
        );
    }

    @Before
    public void setUp() throws DAOException, SQLException {
//        currentConnection = factory.getConnection();
//        currentConnection.setAutoCommit(false);
//        dao = factory.getDao(currentConnection, daoClass);
    }

    @After
    public void tearDown() throws SQLException {
        context().rollback();
        context().close();
    }

    @Override
    public GenericReadonlyDAO dao() {
        return dao;
    }

    @Override
    public Connection context() {
        return connection;
    }
}

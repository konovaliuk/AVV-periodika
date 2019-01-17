package persistence.dao;

import persistence.dao.implsql.DAOFactorySQL;

public abstract class DAOFactory {
    private final static IDAOFactory factory = new DAOFactorySQL();

    public static IDAOFactory getFactory() {
        return factory;
    }
}

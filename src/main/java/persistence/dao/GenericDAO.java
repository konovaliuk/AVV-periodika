package persistence.dao;

import model.Entity;
import persistence.database.DbContext;

import java.io.Serializable;

public interface GenericDAO<T extends Entity<PK>, PK extends Serializable> extends GenericReadonlyDAO<T, PK> {

    T create(T object, DbContext context) throws DAOException;

    T update(T object, DbContext context) throws DAOException;

    Boolean delete(Long id, DbContext context) throws DAOException;

}

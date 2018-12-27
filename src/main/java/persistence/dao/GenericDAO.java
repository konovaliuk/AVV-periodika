package persistence.dao;

import model.Entity;
import persistence.database.DBContext;

import java.io.Serializable;

public interface GenericDAO<T extends Entity<PK>, PK extends Serializable> extends GenericReadonlyDAO<T, PK> {

    T create(T object, DBContext context) throws DAOException;

    T update(T object, DBContext context) throws DAOException;

    Boolean delete(Long id, DBContext context) throws DAOException;

}

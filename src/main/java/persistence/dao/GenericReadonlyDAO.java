package persistence.dao;

import model.Entity;
import persistence.database.DBContext;

import java.io.Serializable;
import java.util.List;

public interface GenericReadonlyDAO<T extends Entity<PK>, PK extends Serializable> {
    List<T> select(String sql, DBContext context, Object[] params) throws DAOException;

    T findById(PK id, DBContext context) throws DAOException;

    boolean exist(PK id, DBContext context) throws DAOException;

    List<T> findAll(DBContext context) throws DAOException;

    PK count(String sql, DBContext context, Object[] params) throws DAOException;

    PK countAll(DBContext context) throws DAOException;

    List<T> findAllWithLimit(Long offset, Long limit, DBContext context) throws DAOException;
}

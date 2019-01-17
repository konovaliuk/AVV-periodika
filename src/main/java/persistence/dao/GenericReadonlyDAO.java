package persistence.dao;

import model.Entity;
import persistence.database.DbContext;

import java.io.Serializable;
import java.util.List;

public interface GenericReadonlyDAO<T extends Entity<PK>, PK extends Serializable> {
    List<T> select(String sql, DbContext context, Object[] params) throws DAOException;

    T findById(PK id, DbContext context) throws DAOException;

    boolean exist(PK id, DbContext context) throws DAOException;

    List<T> findAll(DbContext context) throws DAOException;

    PK count(String sql, DbContext context, Object[] params) throws DAOException;

    PK countAll(DbContext context) throws DAOException;

    List<T> findAllWithLimit(Long offset, Long limit, DbContext context) throws DAOException;
}

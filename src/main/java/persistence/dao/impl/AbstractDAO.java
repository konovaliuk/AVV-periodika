package persistence.dao.impl;

import common.ResourceManager;
import model.Entity;
import persistence.dao.DAOException;
import persistence.dao.GenericDAO;
import persistence.database.DBContext;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDAO<T extends Entity<PK>, PK extends Serializable> extends AbstractReadonlyDAO<T, PK>
        implements GenericDAO<T, PK> {
    private static final int KEY_ID = 1;
    private final String SQL_INSERT;
    private final String SQL_UPDATE;
    private final String SQL_DELETE;
    private final String ERR_NO_GENERATED_KEY;
    private final String ERR_PK_NOT_UNIQUE;

    protected AbstractDAO(ResourceManager sqlParams) {
        super(sqlParams);
        SQL_INSERT = sqlParams.get("sql.insert");
        SQL_UPDATE = sqlParams.get("sql.update");
        SQL_DELETE = sqlParams.get("sql.delete");
        ERR_NO_GENERATED_KEY = sqlParams.get("sql.error.no_generated_key");
        ERR_PK_NOT_UNIQUE = sqlParams.get("sql.error.pk_not_unique");
    }

    protected abstract void setInsertStatementValues(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void setUpdateStatementValues(PreparedStatement statement, T entity) throws SQLException;

    @Override
    public T create(T entity, DBContext context) throws DAOException {
        PK id;
        try (PreparedStatement st = context.getConnection()
                .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            setInsertStatementValues(st, entity);
            int count = st.executeUpdate();
            if (count != 1) {
                throw new DAOException("Insertion: " + ERR_PK_NOT_UNIQUE + count);
            }
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    id = valueOfGenKey(rs);
                } else {
                    throw new DAOException(ERR_NO_GENERATED_KEY);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        entity.setId(id);
        return entity;
    }

    @Override
    public T update(T entity, DBContext context) throws DAOException {
        try (PreparedStatement st = context.getConnection().prepareStatement(SQL_UPDATE)) {
            setUpdateStatementValues(st, entity);
            int count = st.executeUpdate();
            if (count != 1) {
                throw new DAOException("Updating by PK: " + ERR_PK_NOT_UNIQUE + count);
            }
            return entity;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Boolean delete(Long id, DBContext context) throws DAOException {
        try (PreparedStatement st = context.getConnection().prepareStatement(SQL_DELETE)) {
            st.setObject(KEY_ID, id);
            int count = st.executeUpdate();
            if (count != 1) {
                throw new DAOException("Deleting by PK: " + ERR_PK_NOT_UNIQUE + count);
            }
            return true;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

}

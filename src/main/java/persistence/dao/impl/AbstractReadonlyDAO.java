package persistence.dao.impl;

import common.ResourceManager;
import model.Entity;
import persistence.dao.DAOException;
import persistence.dao.GenericReadonlyDAO;
import persistence.database.DBContext;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractReadonlyDAO<T extends Entity<PK>, PK extends Serializable>
        implements GenericReadonlyDAO<T, PK> {
    private static final String SQL_LIMIT = " LIMIT ?,?";
    private final String SQL_SELECT_ALL;
    private final String SQL_SELECT_COUNT;
    private final String SQL_SELECT_BY_ID;
    private final String ERR_PK_NOT_UNIQUE;
    private final String ERR_EMPTY_COUNT_RS;

    public AbstractReadonlyDAO(ResourceManager sqlParams) {
        SQL_SELECT_ALL = sqlParams.get("sql.select_all");
        SQL_SELECT_COUNT = sqlParams.get("sql.select_count");
        SQL_SELECT_BY_ID = sqlParams.get("sql.select_by_id");
        ERR_PK_NOT_UNIQUE = sqlParams.get("sql.error.pk_not_unique");
        ERR_EMPTY_COUNT_RS = sqlParams.get("sql.error.empty_count_rs");
    }

    protected abstract T valueOf(ResultSet rs) throws SQLException;

    protected abstract PK valueOfGenKey(ResultSet rs) throws SQLException;

    private List<T> getAllFromRS(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(valueOf(rs));
        }
        return result;
    }

    @Override
    public PK count(String sql, DBContext context, Object... params) throws DAOException {
        try (PreparedStatement st = context.getConnection().prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return valueOfGenKey(rs);
                }
                throw new DAOException(ERR_EMPTY_COUNT_RS);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public PK countAll(DBContext context) throws DAOException {
        return count(SQL_SELECT_COUNT, context);
    }

    public List<T> select(String sql, DBContext context, Object... params) throws DAOException {
        try (PreparedStatement st = context.getConnection().prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    st.setObject(i + 1, params[i]);
                }
            }
            try (ResultSet rs = st.executeQuery()) {
                return getAllFromRS(rs);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public T findById(PK id, DBContext context) throws DAOException {
        List<T> list = select(SQL_SELECT_BY_ID, context, id);
        if (list.size() != 1) {
            throw new DAOException("Selecting by PK: " + ERR_PK_NOT_UNIQUE + list.size());
        }
        return list.get(0);
    }

    @Override
    public boolean exist(PK id, DBContext context) throws DAOException {
        List<T> list = select(SQL_SELECT_BY_ID, context, id);
        int count = list.size();
        if (count > 1) {
            throw new DAOException("Selecting by PK: " + ERR_PK_NOT_UNIQUE + count);
        }
        return count == 1;
    }

    @Override
    public List<T> findAll(DBContext context) throws DAOException {
        return select(SQL_SELECT_ALL, context);
    }

    @Override
    public List<T> findAllWithLimit(Long offset, Long limit, DBContext context) throws DAOException {
        return select(SQL_SELECT_ALL + SQL_LIMIT, context, offset, limit);

    }


}

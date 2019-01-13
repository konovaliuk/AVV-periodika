package persistence.dao.impl;

import common.ResourceManager;
import model.CategoryType;
import model.PeriodicalCategory;
import persistence.dao.DAOException;
import persistence.dao.PeriodicalCategoryDAO;
import persistence.database.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PeriodicalCategoryDAOImpl extends AbstractDAO<PeriodicalCategory, Long> implements PeriodicalCategoryDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_PERIODICAL_CATEGORY;
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_BY_TYPE = "sql.select_all_by_type";
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_NAME = "sql.select_all.name";
    private static final String SQL_SELECT_TYPE = "sql.select_all.type";
    private static final String SQL_SELECT_DESC = "sql.select_all.description";
    private static final String SQL_EDIT_NAME = "sql.edit.name";
    private static final String SQL_EDIT_TYPE = "sql.edit.type";
    private static final String SQL_EDIT_DESC = "sql.edit.description";
    private static final String SQL_UPDATE_ID = "sql.update.id";

    public PeriodicalCategoryDAOImpl() {
        super(RESOURCES);
    }

    @Override
    public List<PeriodicalCategory> findAllByType(CategoryType type, DBContext context) throws DAOException {
        return select(RESOURCES.get(SQL_SELECT_BY_TYPE), context, type.getId());
    }

    @Override
    protected PeriodicalCategory valueOf(ResultSet rs) throws SQLException {
        return new PeriodicalCategory(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                                      rs.getString(RESOURCES.getInt(SQL_SELECT_NAME)),
                                      CategoryType.findById(rs.getInt(RESOURCES.getInt(SQL_SELECT_TYPE))),
                                      rs.getString(RESOURCES.getInt(SQL_SELECT_DESC)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    protected void setInsertStatementValues(PreparedStatement statement, PeriodicalCategory entity)
            throws SQLException {
        statement.setString(RESOURCES.getInt(SQL_EDIT_NAME), entity.getName());
        statement.setLong(RESOURCES.getInt(SQL_EDIT_TYPE), entity.getType().getId());
        statement.setString(RESOURCES.getInt(SQL_EDIT_DESC), entity.getDescription());
    }

    @Override
    protected void setUpdateStatementValues(PreparedStatement statement, PeriodicalCategory entity)
            throws SQLException {
        setInsertStatementValues(statement, entity);
        statement.setLong(RESOURCES.getInt(SQL_UPDATE_ID), entity.getId());
    }
}

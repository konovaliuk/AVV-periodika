package persistence.dao.impl;

import common.ResourceManager;
import model.CategoryType;
import model.Periodical;
import persistence.dao.DAOException;
import persistence.dao.PeriodicalDAO;
import persistence.database.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PeriodicalDAOImpl extends AbstractDAO<Periodical, Long> implements PeriodicalDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_PERIODICAL;
    private static final String SQL_SELECT_BY_CATEGORY = "sql.select_all_by_category";
    private static final String SQL_LIMIT = " LIMIT ?,?";
    private static final String SQL_COUNT_BY_CATEGORY = "sql.select_count_by_category";
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_CAT_ID = "sql.select_all.periodical_category_id";
    private static final String SQL_SELECT_TITLE = "sql.select_all.title";
    private static final String SQL_SELECT_DESC = "sql.select_all.description";
    private static final String SQL_SELECT_MIN_PERIOD = "sql.select_all.min_subscription_period";
    private static final String SQL_SELECT_ISSUES = "sql.select_all.issues_per_period";
    private static final String SQL_SELECT_PRICE = "sql.select_all.price_per_period";
    private static final String SQL_SELECT_CATNAME = "sql.select_all.category_name";
    private static final String SQL_SELECT_CATTYPE = "sql.select_all.category_type";
    private static final String SQL_EDIT_CAT_ID = "sql.edit.periodical_category_id";
    private static final String SQL_EDIT_TITLE = "sql.edit.title";
    private static final String SQL_EDIT_DESC = "sql.edit.description";
    private static final String SQL_EDIT_MIN_PERIOD = "sql.edit.min_subscription_period";
    private static final String SQL_EDIT_ISSUES = "sql.edit.issues_per_period";
    private static final String SQL_EDIT_PRICE = "sql.edit.price_per_period";
    private static final String SQL_UPDATE_ID = "sql.update.id";

    public PeriodicalDAOImpl() {
        super(RESOURCES);
    }

    @Override
    public List<Periodical> findAllByCategoryWithLimit(Long categoryId, Long offset, Long limit, DBContext context)
            throws DAOException {
        return select(RESOURCES.get(SQL_SELECT_BY_CATEGORY) + SQL_LIMIT, context, categoryId, offset, limit);
    }

    @Override
    public Long countByCategory(Long categoryId, DBContext context) throws DAOException {
        return count(RESOURCES.get(SQL_COUNT_BY_CATEGORY), context, categoryId);
    }

    @Override
    protected Periodical valueOf(ResultSet rs) throws SQLException {
        return new Periodical(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                              rs.getLong(RESOURCES.getInt(SQL_SELECT_CAT_ID)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_TITLE)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_DESC)),
                              rs.getInt(RESOURCES.getInt(SQL_SELECT_MIN_PERIOD)),
                              rs.getInt(RESOURCES.getInt(SQL_SELECT_ISSUES)),
                              rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_PRICE)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_CATNAME)),
                              CategoryType.findById(rs.getInt(RESOURCES.getInt(SQL_SELECT_CATTYPE))));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    protected void setInsertStatementValues(PreparedStatement statement, Periodical entity) throws SQLException {
        statement.setLong(RESOURCES.getInt(SQL_EDIT_CAT_ID), entity.getCategoryId());
        statement.setString(RESOURCES.getInt(SQL_EDIT_TITLE), entity.getTitle());
        statement.setString(RESOURCES.getInt(SQL_EDIT_DESC), entity.getDescription());
        statement.setInt(RESOURCES.getInt(SQL_EDIT_MIN_PERIOD), entity.getMinSubscriptionPeriod());
        statement.setInt(RESOURCES.getInt(SQL_EDIT_ISSUES), entity.getIssuesPerPeriod());
        statement.setBigDecimal(RESOURCES.getInt(SQL_EDIT_PRICE), entity.getPricePerPeriod());
    }

    @Override
    protected void setUpdateStatementValues(PreparedStatement statement, Periodical entity) throws SQLException {
        setInsertStatementValues(statement, entity);
        statement.setLong(RESOURCES.getInt(SQL_UPDATE_ID), entity.getId());
    }
}

package persistence.dao.impl;

import common.ResourceManager;
import model.PeriodicalInfo;
import persistence.dao.PeriodicalInfoDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodicalInfoDAOImpl extends AbstractReadonlyDAO<PeriodicalInfo, Long> implements PeriodicalInfoDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_PERIODICAL_INFO;
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
    private static final String SQL_SELECT_SUBINDEX = "sql.select_all.subscription_index";
    private static final String SQL_SELECT_CATNAME = "sql.select_all.category_name";
    private static final String SQL_SELECT_CATTYPE = "sql.select_all.category_type";

    public PeriodicalInfoDAOImpl() {
        super(RESOURCES);
    }

    @Override
    protected PeriodicalInfo valueOf(ResultSet rs) throws SQLException {
        return new PeriodicalInfo(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                              rs.getLong(RESOURCES.getInt(SQL_SELECT_CAT_ID)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_TITLE)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_DESC)),
                              rs.getInt(RESOURCES.getInt(SQL_SELECT_MIN_PERIOD)),
                              rs.getInt(RESOURCES.getInt(SQL_SELECT_ISSUES)),
                              rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_PRICE)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_SUBINDEX)),
                              rs.getString(RESOURCES.getInt(SQL_SELECT_CATNAME)),
                              rs.getInt(RESOURCES.getInt(SQL_SELECT_CATTYPE)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }
}

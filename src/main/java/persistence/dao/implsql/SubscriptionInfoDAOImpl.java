package persistence.dao.implsql;

import common.ResourceManager;
import model.CategoryType;
import model.Periodical;
import model.SubscriptionInfo;
import model.SubscriptionStatus;
import persistence.dao.DAOException;
import persistence.dao.SubscriptionInfoDAO;
import persistence.database.DbContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;

public class SubscriptionInfoDAOImpl extends AbstractReadonlyDAO<SubscriptionInfo, Long>
        implements SubscriptionInfoDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_SUBSCRIPTION_INFO;
    private static final String SQL_SELECT_BY_USER = "sql.select_all_by_user";
    private static final String SQL_SUFFIX_NO_PAYMENT = "sql.select_suffix_by_user_no_payment";
    private static final String SQL_SUFFIX_ACTIVE = "sql.select_suffix_by_user_active";
    private static final String SQL_SUFFIX_FINISHED = "sql.select_suffix_by_user_finished";
    private static final String SQL_SUFFIX_BY_PAYMENT = "sql.select_suffix_by_payment";
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_USER_ID = "sql.select_all.user_id";
    private static final String SQL_SELECT_PERIODICAL_ID = "sql.select_all.periodical_id";
    private static final String SQL_SELECT_PAYMENT_ID = "sql.select_all.payment_id";
    private static final String SQL_SELECT_PERIOD_START = "sql.select_all.period_start";
    private static final String SQL_SELECT_PERIOD_COUNT = "sql.select_all.period_count";
    private static final String SQL_SELECT_PERIOD_END = "sql.select_all.period_end";
    private static final String SQL_SELECT_QUANTITY = "sql.select_all.quantity";
    private static final String SQL_SELECT_SUM = "sql.select_all.sum";
    private static final String SQL_SELECT_CAT_ID = "sql.select_all.periodical_category_id";
    private static final String SQL_SELECT_TITLE = "sql.select_all.title";
    private static final String SQL_SELECT_DESC = "sql.select_all.description";
    private static final String SQL_SELECT_MIN_PERIOD = "sql.select_all.min_subscription_period";
    private static final String SQL_SELECT_ISSUES = "sql.select_all.issues_per_period";
    private static final String SQL_SELECT_PRICE = "sql.select_all.price_per_period";
    private static final String SQL_SELECT_CATNAME = "sql.select_all.category_name";
    private static final String SQL_SELECT_CATTYPE = "sql.select_all.category_type";

    SubscriptionInfoDAOImpl() {
        super(RESOURCES);
    }

    @Override
    public List<SubscriptionInfo> findAllByUser(Long userId, DbContext context)
            throws DAOException {
        return select(RESOURCES.get(SQL_SELECT_BY_USER), context, userId);
    }

    @Override
    public List<SubscriptionInfo> findAllByUserAndStatus(Long userId, SubscriptionStatus status, DbContext context)
            throws DAOException {
        String sql = RESOURCES.get(SQL_SELECT_BY_USER) + " ";
        switch (status) {
            case SAVED:
                return select(sql + RESOURCES.get(SQL_SUFFIX_NO_PAYMENT), context, userId);
            case ACTIVE:
                return select(sql + RESOURCES.get(SQL_SUFFIX_ACTIVE), context, userId);
            case FINISHED:
                return select(sql + RESOURCES.get(SQL_SUFFIX_FINISHED), context, userId);
        }
        return null;
    }

    @Override
    public List<SubscriptionInfo> findAllByPayment(Long userId, Long paymentId, DbContext context) throws DAOException {
        return select(RESOURCES.get(SQL_SELECT_BY_USER) + " " + RESOURCES.get(SQL_SUFFIX_BY_PAYMENT),
                      context,
                      userId,
                      paymentId);
    }

    @Override
    protected SubscriptionInfo valueOf(ResultSet rs) throws SQLException {

        Periodical periodical = new Periodical(rs.getLong(RESOURCES.getInt(SQL_SELECT_PERIODICAL_ID)),
                                               rs.getLong(RESOURCES.getInt(SQL_SELECT_CAT_ID)),
                                               rs.getString(RESOURCES.getInt(SQL_SELECT_TITLE)),
                                               rs.getString(RESOURCES.getInt(SQL_SELECT_DESC)),
                                               rs.getInt(RESOURCES.getInt(SQL_SELECT_MIN_PERIOD)),
                                               rs.getInt(RESOURCES.getInt(SQL_SELECT_ISSUES)),
                                               rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_PRICE)),
                                               rs.getString(RESOURCES.getInt(SQL_SELECT_CATNAME)),
                                               CategoryType.findById(rs.getInt(RESOURCES.getInt(SQL_SELECT_CATTYPE))));

        return new SubscriptionInfo(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                                    rs.getLong(RESOURCES.getInt(SQL_SELECT_USER_ID)),
                                    rs.getLong(RESOURCES.getInt(SQL_SELECT_PERIODICAL_ID)),
                                    rs.getLong(RESOURCES.getInt(SQL_SELECT_PAYMENT_ID)),
                                    YearMonth.from(rs.getDate(RESOURCES.getInt(SQL_SELECT_PERIOD_START)).toLocalDate()),
                                    rs.getInt(RESOURCES.getInt(SQL_SELECT_PERIOD_COUNT)),
                                    YearMonth.from(rs.getDate(RESOURCES.getInt(SQL_SELECT_PERIOD_END)).toLocalDate()),
                                    rs.getInt(RESOURCES.getInt(SQL_SELECT_QUANTITY)),
                                    rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_SUM)),
                                    periodical);
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }
}

package persistence.dao.impl;

import common.ResourceManager;
import model.Subscription;
import persistence.dao.SubscriptionDAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.YearMonth;

import static common.ResourceManager.NULL_ID;

public class SubscriptionDAOImpl extends AbstractDAO<Subscription, Long> implements SubscriptionDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_SUBSCRIPTION;
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
    private static final String SQL_EDIT_USER_ID = "sql.edit.user_id";
    private static final String SQL_EDIT_PERIODICAL_ID = "sql.edit.periodical_id";
    private static final String SQL_EDIT_PAYMENT_ID = "sql.edit.payment_id";
    private static final String SQL_EDIT_PERIOD_START = "sql.edit.period_start";
    private static final String SQL_EDIT_PERIOD_COUNT = "sql.edit.period_count";
    private static final String SQL_EDIT_PERIOD_END = "sql.edit.period_end";
    private static final String SQL_EDIT_QUANTITY = "sql.edit.quantity";
    private static final String SQL_EDIT_SUM = "sql.edit.sum";
    private static final String SQL_UPDATE_ID = "sql.update.id";

    public SubscriptionDAOImpl() {
        super(RESOURCES);
    }

    @Override
    protected Subscription valueOf(ResultSet rs) throws SQLException {
        return new Subscription(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                                rs.getLong(RESOURCES.getInt(SQL_SELECT_USER_ID)),
                                rs.getLong(RESOURCES.getInt(SQL_SELECT_PERIODICAL_ID)),
                                rs.getLong(RESOURCES.getInt(SQL_SELECT_PAYMENT_ID)),
                                YearMonth.from(rs.getDate(RESOURCES.getInt(SQL_SELECT_PERIOD_START)).toLocalDate()),
                                rs.getInt(RESOURCES.getInt(SQL_SELECT_PERIOD_COUNT)),
                                YearMonth.from(rs.getDate(RESOURCES.getInt(SQL_SELECT_PERIOD_END)).toLocalDate()),
                                rs.getInt(RESOURCES.getInt(SQL_SELECT_QUANTITY)),
                                rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_SUM)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    protected void setInsertStatementValues(PreparedStatement statement, Subscription entity) throws SQLException {
        statement.setLong(RESOURCES.getInt(SQL_EDIT_USER_ID), entity.getUserId());
        statement.setLong(RESOURCES.getInt(SQL_EDIT_PERIODICAL_ID), entity.getPeriodicalId());
        if (entity.getPaymentId() == null || entity.getPaymentId() == NULL_ID) {
            statement.setNull(RESOURCES.getInt(SQL_EDIT_PAYMENT_ID), Types.INTEGER);
        } else {
            statement.setLong(RESOURCES.getInt(SQL_EDIT_PAYMENT_ID), entity.getPaymentId());
        }
        statement.setDate(RESOURCES.getInt(SQL_EDIT_PERIOD_START),
                          Date.valueOf(LocalDate.from(entity.getPeriodStart().atDay(1))));
        statement.setInt(RESOURCES.getInt(SQL_EDIT_PERIOD_COUNT), entity.getPeriodCount());
        statement.setDate(RESOURCES.getInt(SQL_EDIT_PERIOD_END),
                          Date.valueOf(LocalDate.from(entity.getPeriodEnd().atDay(1))));
        statement.setInt(RESOURCES.getInt(SQL_EDIT_QUANTITY), entity.getQuantity());
        statement.setBigDecimal(RESOURCES.getInt(SQL_EDIT_SUM), entity.getSum());
    }

    @Override
    protected void setUpdateStatementValues(PreparedStatement statement, Subscription entity) throws SQLException {
        setInsertStatementValues(statement, entity);
        statement.setLong(RESOURCES.getInt(SQL_UPDATE_ID), entity.getId());
    }
}

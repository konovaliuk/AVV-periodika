package persistence.dao.impl;

import common.ResourceManager;
import model.Payment;
import persistence.dao.DAOException;
import persistence.dao.PaymentDAO;
import persistence.database.DBContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class PaymentDAOImpl extends AbstractDAO<Payment, Long> implements PaymentDAO {
    private static final ResourceManager RESOURCES = ResourceManager.RM_DAO_PAYMENT;
    private static final String SQL_SELECT_BY_USER = "sql.select_all_by_user";
    private static final int KEY_ID = 1;
    private static final String SQL_SELECT_ID = "sql.select_all.id";
    private static final String SQL_SELECT_DATE = "sql.select_all.date";
    private static final String SQL_SELECT_SUM = "sql.select_all.sum";
    private static final String SQL_EDIT_DATE = "sql.edit.date";
    private static final String SQL_EDIT_SUM = "sql.edit.sum";
    private static final String SQL_UPDATE_ID = "sql.update.id";
    private static final String ERROR_INCORRECT_DAO_ACTION = "Incorrect DAO operation!";

    public PaymentDAOImpl() {
        super(RESOURCES);
    }

    @Override
    public Payment update(Payment entity, DBContext context) throws DAOException {
        throw new DAOException(ERROR_INCORRECT_DAO_ACTION);
    }

    @Override
    public Boolean delete(Long id, DBContext context) throws DAOException {
        throw new DAOException(ERROR_INCORRECT_DAO_ACTION);
    }

    @Override
    public List<Payment> findAllByUser(Long userId, DBContext context)
            throws DAOException {
        return select(RESOURCES.get(SQL_SELECT_BY_USER), context, userId);
    }

    @Override
    protected Payment valueOf(ResultSet rs) throws SQLException {
        return new Payment(rs.getLong(RESOURCES.getInt(SQL_SELECT_ID)),
                           rs.getObject(RESOURCES.getInt(SQL_SELECT_DATE), Instant.class),
                           rs.getBigDecimal(RESOURCES.getInt(SQL_SELECT_SUM)));
    }

    @Override
    protected Long valueOfGenKey(ResultSet rs) throws SQLException {
        return rs.getLong(KEY_ID);
    }

    @Override
    protected void setInsertStatementValues(PreparedStatement statement, Payment entity) throws SQLException {
        statement.setObject(RESOURCES.getInt(SQL_EDIT_DATE), entity.getDate());
        statement.setBigDecimal(RESOURCES.getInt(SQL_EDIT_SUM), entity.getSum());
    }

    @Override
    protected void setUpdateStatementValues(PreparedStatement statement, Payment entity) throws SQLException {
        statement.setObject(RESOURCES.getInt(SQL_EDIT_DATE), entity.getDate());
        statement.setBigDecimal(RESOURCES.getInt(SQL_EDIT_SUM), entity.getSum());
        statement.setLong(RESOURCES.getInt(SQL_UPDATE_ID), entity.getId());
    }
}

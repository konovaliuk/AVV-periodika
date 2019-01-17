package persistence.dao;

import model.Payment;
import persistence.database.DbContext;

import java.util.List;

public interface PaymentDAO extends GenericDAO<Payment, Long> {
    List<Payment> findAllByUser(Long userId, DbContext context)
            throws DAOException;
}

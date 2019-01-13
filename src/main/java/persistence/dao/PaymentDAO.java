package persistence.dao;

import model.Payment;
import persistence.database.DBContext;

import java.util.List;

public interface PaymentDAO extends GenericDAO<Payment, Long> {
    List<Payment> findAllByUser(Long userId, DBContext context)
            throws DAOException;
}

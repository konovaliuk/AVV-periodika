package persistence.dao;

import model.SubscriptionInfo;
import model.SubscriptionStatus;
import persistence.database.DbContext;

import java.util.List;

public interface SubscriptionInfoDAO extends GenericReadonlyDAO<SubscriptionInfo, Long> {
    List<SubscriptionInfo> findAllByUser(Long userId, DbContext context)
            throws DAOException;

    List<SubscriptionInfo> findAllByUserAndStatus(Long userId, SubscriptionStatus status, DbContext context)
            throws DAOException;

    List<SubscriptionInfo> findAllByPayment(Long userId, Long paymentId, DbContext context)
            throws DAOException;
}

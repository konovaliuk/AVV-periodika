package persistence.dao;

import model.SubscriptionInfo;
import model.SubscriptionStatus;
import persistence.database.DBContext;

import java.util.List;

public interface SubscriptionInfoDAO extends GenericReadonlyDAO<SubscriptionInfo, Long> {
    List<SubscriptionInfo> findAllByUser(Long userId, DBContext context)
            throws DAOException;

    List<SubscriptionInfo> findAllByUserAndStatus(Long userId, SubscriptionStatus status, DBContext context)
            throws DAOException;
}

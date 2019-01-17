package persistence.dao.implsql;

import persistence.dao.*;

public class DAOFactorySQL implements IDAOFactory {
    private final static UserDAO USER_DAO = new UserDAOImpl();
    private final static UserTypeDAO USER_TYPE_DAO = new UserTypeDAOImpl();
    private final static PeriodicalCategoryDAO PERIODICAL_CATEGORY_DAO = new PeriodicalCategoryDAOImpl();
    private final static PeriodicalDAO PERIODICAL_DAO = new PeriodicalDAOImpl();
    private final static SubscriptionDAO SUBSCRIPTION_DAO = new SubscriptionDAOImpl();
    private final static SubscriptionInfoDAO SUBSCRIPTION_INFO_DAO = new SubscriptionInfoDAOImpl();
    private final static PaymentDAO PAYMENT_DAO = new PaymentDAOImpl();

    public UserDAO getUserDAO() {
        return USER_DAO;
    }

    public UserTypeDAO getUserTypeDAO() {
        return USER_TYPE_DAO;
    }

    public PeriodicalCategoryDAO getPeriodicalCategoryDAO() {
        return PERIODICAL_CATEGORY_DAO;
    }

    public PeriodicalDAO getPeriodicalDAO() {
        return PERIODICAL_DAO;
    }

    public SubscriptionDAO getSubscriptionDAO() {
        return SUBSCRIPTION_DAO;
    }

    public SubscriptionInfoDAO getSubscriptionInfoDAO() {
        return SUBSCRIPTION_INFO_DAO;
    }

    public PaymentDAO getPaymentDAO() {
        return PAYMENT_DAO;
    }
}

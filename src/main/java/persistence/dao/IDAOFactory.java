package persistence.dao;

public interface IDAOFactory {
    UserDAO getUserDAO();

    UserTypeDAO getUserTypeDAO();

    PeriodicalCategoryDAO getPeriodicalCategoryDAO();

    PeriodicalDAO getPeriodicalDAO();

    SubscriptionDAO getSubscriptionDAO();

    SubscriptionInfoDAO getSubscriptionInfoDAO();

    PaymentDAO getPaymentDAO();
}

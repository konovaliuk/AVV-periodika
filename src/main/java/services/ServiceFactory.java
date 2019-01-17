package services;

public abstract class ServiceFactory {
    private final static UserService USER_SERVICE = new UserService();
    private final static CatalogService CATALOG_SERVICE = new CatalogService();
    private final static PeriodicalService PERIODICAL_SERVICE = new PeriodicalService();
    private final static SubscriptionService SUBSCRIPTION_SERVICE = new SubscriptionService();
    private final static PaymentService PAYMENT_SERVICE = new PaymentService();

    public static UserService getUserService() {
        return ServiceFactory.USER_SERVICE;
    }

    public static CatalogService getCatalogService() {
        return ServiceFactory.CATALOG_SERVICE;
    }

    public static SubscriptionService getSubscriptionService() {
        return ServiceFactory.SUBSCRIPTION_SERVICE;
    }

    public static PeriodicalService getPeriodicalService() {
        return PERIODICAL_SERVICE;
    }

    public static PaymentService getPaymentService() {
        return PAYMENT_SERVICE;
    }
}

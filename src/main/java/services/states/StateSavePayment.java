package services.states;

import model.Payment;

public class StateSavePayment extends GenericStateSave<Payment> {
    private String[] subscriptionIds;

    public String[] getSubscriptionIds() {
        return subscriptionIds;
    }

    public void setSubscriptionIds(String[] subscriptionIds) {
        this.subscriptionIds = subscriptionIds;
    }
}

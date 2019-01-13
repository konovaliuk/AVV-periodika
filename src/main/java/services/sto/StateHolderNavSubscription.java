package services.sto;

import model.SubscriptionInfo;
import model.User;

public final class StateHolderNavSubscription {
    private State resultState;
    private long subscriptionId;
    private User user;
    private SubscriptionInfo tempSubscription;
    private SubscriptionInfo subscription;

    public StateHolderNavSubscription() { }

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionInfo getTempSubscription() {
        return tempSubscription;
    }

    public void setTempSubscription(SubscriptionInfo tempSubscription) {
        this.tempSubscription = tempSubscription;
    }

    public SubscriptionInfo getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionInfo subscription) {
        this.subscription = subscription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_SUBSCRIPTION,
        EDIT_SUBSCRIPTION,
        VIEW_SUBSCRIPTION
    }
}

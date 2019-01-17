package services.states;

import model.SubscriptionInfo;

public class StateNavSubscription extends GenericStateNavigate<SubscriptionInfo, StateNavSubscription.State> {
    public StateNavSubscription() { }

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_SUBSCRIPTION,
        EDIT_SUBSCRIPTION,
        VIEW_SUBSCRIPTION
    }
}

package services.sto;

import model.Payment;
import model.Subscription;

import java.util.List;

public final class StateHolderPayment extends StateHolderSaveEntity<Payment> {
    private List<Subscription> subscriptions;

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}

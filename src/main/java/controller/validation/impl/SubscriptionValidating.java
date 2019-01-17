package controller.validation.impl;

import controller.validation.AbstractValidating;
import controller.validation.ValidationBehavior;
import controller.validation.ValidationData;
import model.Entity;
import model.Subscription;

import static common.ResourceManager.MESSAGE_SUBSCRIPTION_WRONG_PERIOD_START;
import static common.ViewConstants.*;

public class SubscriptionValidating extends AbstractValidating implements ValidationBehavior<Subscription> {
    public SubscriptionValidating(String language) {
        super(language);
    }

    @Override
    public ValidationData match(Subscription subscription) {
        ValidationData validationInfo = new ValidationData();
        if (subscription.getUserId() == null || subscription.getUserId() == Entity.NULL_ID) {
            validationInfo.setError(INPUT_SUBSCRIPTION_USER_ID);
        }
        if (subscription.getPeriodicalId() == null || subscription.getPeriodicalId() == Entity.NULL_ID) {
            validationInfo.setError(INPUT_SUBSCRIPTION_PERIODICAL_ID);
        }
        if (subscription.getPeriodStart() == null ||
            subscription.getPeriodStart().isBefore(subscription.getMinPeriodStart())) {
            validationInfo.setError(INPUT_SUBSCRIPTION_PERIOD_START, MESSAGE_SUBSCRIPTION_WRONG_PERIOD_START);
        }
        if (subscription.getPeriodCount() == null || subscription.getPeriodCount() < 1) {
            validationInfo.setError(INPUT_SUBSCRIPTION_PERIOD_COUNT);
        }
        if (subscription.getQuantity() == null || subscription.getQuantity() < 1) {
            validationInfo.setError(INPUT_SUBSCRIPTION_QUANTITY);
        }
        return validationInfo;
    }

}

package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import model.SubscriptionInfo;
import model.User;
import org.apache.commons.lang3.math.NumberUtils;
import services.PeriodicalService;

import static common.ResourceManager.MESSAGE_COMMAND_EXECUTION_ERROR;
import static common.ResourceManager.NULL_ID;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_SUBSCRIPTION;
import static common.ViewConstants.ATTR_NAME_TEMP_SUBSCRIPTION;
import static common.ViewConstants.PARAM_NAME_PERIODICAL_ID;

public class CommandSubscriptionNew implements Command {

    private static final int NEW_SUBSCRIPTION_QUANTITY = 1;

    @Override
    public CommandResult execute(SessionRequestContent context) {
        SubscriptionInfo subscription = initEntity(context);
        if (subscription == null) {
            context.setMessageDanger(MESSAGE_COMMAND_EXECUTION_ERROR);
            return CommandResult.redirect(null);
        }
        context.setSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION, subscription);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_SUBSCRIPTION));
    }

    private SubscriptionInfo initEntity(SessionRequestContent context) {
        User user = context.getCurrentUser();
        if (user == null) {
            return null;
        }
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID), NULL_ID);
        if (periodicalId == NULL_ID) {
            return null;
        }
        Periodical periodical = PeriodicalService.findPeriodical(periodicalId);
        if (periodical == null) {
            return null;
        }
        SubscriptionInfo subscription = new SubscriptionInfo();
        subscription.setUserId(user.getId());
        subscription.setPaymentId(NULL_ID);
        subscription.setPeriodicalId(periodicalId);
        subscription.setPeriodStart(subscription.getMinPeriodStart());
        subscription.setPeriodCount(Math.max(1, 12 / periodical.getMinSubscriptionPeriod()));
        subscription.setQuantity(NEW_SUBSCRIPTION_QUANTITY);
        subscription.setPeriodical(periodical);
        return subscription;
    }
}

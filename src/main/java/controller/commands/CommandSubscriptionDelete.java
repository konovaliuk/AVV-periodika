package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.commons.lang3.math.NumberUtils;
import services.SubscriptionService;

import static common.ResourceManager.*;
import static common.ViewConstants.PARAM_NAME_SUBSCRIPTION_ID;


public class CommandSubscriptionDelete implements Command {
    @Override
    public CommandResult execute(SessionRequestContent context) {
        long subscriptionId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_SUBSCRIPTION_ID), NULL_ID);
        if (subscriptionId == NULL_ID) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_WRONG_PARAMETERS));
            return CommandResult.redirect(null);
        }
        if (SubscriptionService.serveDeleteSubscription(subscriptionId)) {
            context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_SUBSCRIPTION_DELETE_SUCCESS));
        } else {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_SUBSCRIPTION_DELETE_ERROR));
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
    }
}

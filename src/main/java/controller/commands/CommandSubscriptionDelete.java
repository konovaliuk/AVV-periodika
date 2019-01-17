package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;

import static common.ResourceManager.*;
import static common.ViewConstants.PARAM_NAME_SUBSCRIPTION_ID;


public class CommandSubscriptionDelete implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        long subscriptionId =
                NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_SUBSCRIPTION_ID), Entity.NULL_ID);
        if (subscriptionId == Entity.NULL_ID) {
            context.setMessageWarning(MESSAGE_WRONG_PARAMETERS);
            return CommandResult.redirect(null);
        }
        if (ServiceFactory.getSubscriptionService().serveDeleteSubscription(subscriptionId)) {
            context.setMessageSuccess(MESSAGE_SUBSCRIPTION_DELETE_SUCCESS);
        } else {
            context.setMessageDanger(MESSAGE_SUBSCRIPTION_DELETE_ERROR);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
    }
}

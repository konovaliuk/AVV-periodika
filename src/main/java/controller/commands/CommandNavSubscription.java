package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.SubscriptionInfo;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.StateNavSubscription;

import static common.ResourceManager.MESSAGE_COMMAND_EXECUTION_ERROR;
import static common.ResourceManager.PAGE_SUBSCRIPTION;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CATALOG;
import static common.ViewConstants.ATTR_NAME_EDIT_MODE;
import static common.ViewConstants.ATTR_NAME_NEW_MODE;
import static common.ViewConstants.ATTR_NAME_TEMP_SUBSCRIPTION;
import static common.ViewConstants.PARAM_NAME_SUBSCRIPTION_ID;

public class CommandNavSubscription implements Command {
    private static final String ATTR_NAME_SUBSCRIPTION = "subscription_info";

    @Override
    public CommandResult execute(HttpContext context) {
        StateNavSubscription state = readState(context);
        ServiceFactory.getSubscriptionService().serveNavSubscription(state);
        return writeNewState(state, context);
    }

    private StateNavSubscription readState(HttpContext context) {
        StateNavSubscription state = new StateNavSubscription();
        state.setUser(context.getCurrentUser());
        state.setTempEntity((SubscriptionInfo) context.getSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION));
        context.removeSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION);
        state.setEntityId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_SUBSCRIPTION_ID),
                                             Entity.NULL_ID));
        return state;
    }

    private CommandResult writeNewState(StateNavSubscription state, HttpContext context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(MESSAGE_COMMAND_EXECUTION_ERROR);
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_SUBSCRIPTION:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_SUBSCRIPTION:
            case VIEW_SUBSCRIPTION:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE,
                                            state.getEntity().getPaymentId() == null ||
                                            state.getEntity().getPaymentId() == Entity.NULL_ID);
                context.setRequestAttribute(ATTR_NAME_SUBSCRIPTION, state.getEntity());
                context.setRequestAttribute(ATTR_NAME_TEMP_SUBSCRIPTION, state.getTempEntity());
            default:
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_SUBSCRIPTION));
        }
    }

}

package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.SubscriptionInfo;
import org.apache.commons.lang3.math.NumberUtils;
import services.SubscriptionService;
import services.sto.StateHolderNavSubscription;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandNavSubscription implements Command {

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderNavSubscription state = readState(context);
        SubscriptionService.serveNavSubscription(state);
        return writeNewState(state, context);
    }

    private StateHolderNavSubscription readState(SessionRequestContent context) {
        StateHolderNavSubscription state = new StateHolderNavSubscription();
        state.setUser(context.getCurrentUser());
        state.setTempSubscription((SubscriptionInfo) context.getSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION));
        context.removeSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION);
        state.setSubscriptionId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_SUBSCRIPTION_ID), NULL_ID));
        return state;
    }

    private CommandResult writeNewState(StateHolderNavSubscription state, SessionRequestContent context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_COMMAND_EXECUTION_ERROR));
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_SUBSCRIPTION:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_SUBSCRIPTION:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            case VIEW_SUBSCRIPTION:
                context.setRequestAttribute(ATTR_NAME_SUBSCRIPTION, state.getSubscription());
                context.setRequestAttribute(ATTR_NAME_TEMP_SUBSCRIPTION, state.getTempSubscription());
            default:
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_SUBSCRIPTION));
        }
    }

}

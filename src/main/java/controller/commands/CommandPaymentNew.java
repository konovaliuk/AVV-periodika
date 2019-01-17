package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import services.ServiceFactory;
import services.states.StateSavePayment;

import static common.ResourceManager.MESSAGE_VALIDATION_ERROR;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CABINET;
import static common.ResourceManager.URL_PAYMENT;
import static common.ViewConstants.ATTR_NAME_LANGUAGE;
import static common.ViewConstants.ATTR_NAME_TEMP_PAYMENT;
import static common.ViewConstants.ATTR_NAME_VALIDATION_INFO;
import static common.ViewConstants.INPUT_SUBSCRIPTION_ID;

public class CommandPaymentNew implements Command {
    private static final String TXT_SUBSCRIPTIONS = "#subscriptions";

    @Override
    public CommandResult execute(HttpContext context) {
        StateSavePayment state = readState(context);
        ServiceFactory.getPaymentService().serveNewPayment(state);
        return writeNewState(state, context);
    }

    private StateSavePayment readState(HttpContext context) {
        StateSavePayment state = new StateSavePayment();
        state.setSubscriptionIds(context.getRequestParameterValues(INPUT_SUBSCRIPTION_ID));
        state.setUser(context.getCurrentUser());
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        return state;
    }

    private CommandResult writeNewState(StateSavePayment state, HttpContext context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET) + TXT_SUBSCRIPTIONS);
            case SUCCESS:
                context.setSessionAttribute(ATTR_NAME_TEMP_PAYMENT, state.getEntity());
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_PAYMENT));
    }
}

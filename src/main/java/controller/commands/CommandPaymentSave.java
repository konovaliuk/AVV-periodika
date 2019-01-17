package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Payment;
import services.ServiceFactory;
import services.states.StateSavePayment;

import static common.ResourceManager.*;
import static common.ViewConstants.ATTR_NAME_TEMP_PAYMENT;
import static common.ViewConstants.PARAM_NAME_PAYMENT_ID;

public class CommandPaymentSave implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        StateSavePayment state = readState(context);
        ServiceFactory.getPaymentService().serveSavePayment(state);
        return writeNewState(state, context);
    }

    private StateSavePayment readState(HttpContext context) {
        StateSavePayment state = new StateSavePayment();
        state.setUser(context.getCurrentUser());
        state.setEntity((Payment) context.getSessionAttribute(ATTR_NAME_TEMP_PAYMENT));
        context.removeSessionAttribute(ATTR_NAME_TEMP_PAYMENT);
        return state;
    }

    private CommandResult writeNewState(StateSavePayment state, HttpContext context) {
        switch (state.getResultState()) {
            case ERROR_ENTITY_NOT_SAVED:
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_PAYMENT_SAVE_ERROR);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case SUCCESS:
                context.setMessageSuccess(MESSAGE_PAYMENT_SAVE_SUCCESS);
        }
        return CommandResult.redirect(
                RM_VIEW_PAGES.get(URL_PAYMENT) + "?" + PARAM_NAME_PAYMENT_ID + "=" + state.getEntity().getId());
    }

}

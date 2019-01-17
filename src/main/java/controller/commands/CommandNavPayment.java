package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Payment;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.StateSavePayment;

import static common.ResourceManager.PAGE_PAYMENT;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CABINET;
import static common.ViewConstants.ATTR_NAME_EDIT_MODE;
import static common.ViewConstants.ATTR_NAME_TEMP_PAYMENT;
import static common.ViewConstants.PARAM_NAME_PAYMENT_ID;
import static model.Entity.NULL_ID;

public class CommandNavPayment implements Command {

    @Override
    public CommandResult execute(HttpContext context) {
        StateSavePayment state = readState(context);
        ServiceFactory.getPaymentService().serveNavPayment(state);
        return writeNewState(state, context);
    }

    private StateSavePayment readState(HttpContext context) {
        StateSavePayment state = new StateSavePayment();
        state.setUser(context.getCurrentUser());
        long paymentId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PAYMENT_ID), NULL_ID);
        if (paymentId == NULL_ID) {
            state.setEntity((Payment) context.getSessionAttribute(ATTR_NAME_TEMP_PAYMENT));
        } else {
            state.setEntity(new Payment(paymentId, null, null));
        }
        context.removeSessionAttribute(ATTR_NAME_TEMP_PAYMENT);
        return state;
    }

    private CommandResult writeNewState(StateSavePayment state, HttpContext context) {
        switch (state.getResultState()) {
            case ERROR_ENTITY_NOT_SAVED:
            case ERROR_WRONG_PARAMETERS:
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case SUCCESS:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
                context.setSessionAttribute(ATTR_NAME_TEMP_PAYMENT, state.getEntity());
        }
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_PAYMENT));
    }

}

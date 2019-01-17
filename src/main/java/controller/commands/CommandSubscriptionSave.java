package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.SubscriptionInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.ServiceFactory;
import services.states.GenericStateSave;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandSubscriptionSave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandSubscriptionSave.class);

    @Override
    public CommandResult execute(HttpContext context) {
        GenericStateSave<SubscriptionInfo> state = readState(context);
        ServiceFactory.getSubscriptionService().serveSaveSubscription(state);
        return writeNewState(state, context);
    }

    private GenericStateSave<SubscriptionInfo> readState(HttpContext context) {
        GenericStateSave<SubscriptionInfo> state = new GenericStateSave<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        state.setUser(context.getCurrentUser());
        long subscriptionId = NumberUtils.toLong(context.getRequestParameter(INPUT_SUBSCRIPTION_ID), Entity.NULL_ID);
        state.setEntity(
                new SubscriptionInfo(subscriptionId == Entity.NULL_ID ? null : subscriptionId,
                                     NumberUtils.toLong(context.getRequestParameter(INPUT_USER_ID), Entity.NULL_ID),
                                     NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_ID),
                                                        Entity.NULL_ID),
                                     Entity.NULL_ID,
                                     parseYearMonth(context.getRequestParameter(INPUT_SUBSCRIPTION_PERIOD_START)),
                                     NumberUtils.toInt(context.getRequestParameter(INPUT_SUBSCRIPTION_PERIOD_COUNT), 0),
                                     null,
                                     NumberUtils.toInt(context.getRequestParameter(INPUT_SUBSCRIPTION_QUANTITY), 0),
                                     null,
                                     null));
        return state;
    }

    private YearMonth parseYearMonth(String inputDate) {
        try {
            return YearMonth.parse(inputDate);
        } catch (DateTimeParseException e) {
            LOGGER.error(e);
        }
        return null;
    }

    private CommandResult writeNewState(GenericStateSave<SubscriptionInfo> state,
                                        HttpContext context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        Long id = state.getEntity().getId();
        String errorPath = RM_VIEW_PAGES.get(URL_SUBSCRIPTION) +
                           (id == null ? "" : "?" + PARAM_NAME_SUBSCRIPTION_ID + "=" + id);
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION, state.getEntity());
                return CommandResult.redirect(errorPath);
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(MESSAGE_SUBSCRIPTION_SAVE_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_SUBSCRIPTION, state.getEntity());
                return CommandResult.redirect(errorPath);
            case SUCCESS:
                context.setMessageSuccess(MESSAGE_SUBSCRIPTION_SAVE_SUCCESS);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET) + "#id" + id);
    }
}

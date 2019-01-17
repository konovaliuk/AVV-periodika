package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.GenericStateSave;

import java.math.BigDecimal;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandPeriodicalSave implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        GenericStateSave<Periodical> state = readState(context);
        ServiceFactory.getPeriodicalService().serveSave(state);
        return writeNewState(state, context);
    }

    private GenericStateSave<Periodical> readState(HttpContext context) {
        GenericStateSave<Periodical> state = new GenericStateSave<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_ID), Entity.NULL_ID);
        state.setEntity(
                new Periodical(periodicalId == Entity.NULL_ID ? null : periodicalId,
                               NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_CATEGORY_ID),
                                                  Entity.NULL_ID),
                               context.getRequestParameter(INPUT_PERIODICAL_TITLE),
                               context.getRequestParameter(INPUT_PERIODICAL_DESCRIPTION),
                               NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_MIN_PERIOD), 0),
                               NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_ISSUES), 0),
                               BigDecimal.valueOf(NumberUtils.toDouble(context.getRequestParameter(
                                       INPUT_PERIODICAL_PRICE), 0))));
        return state;
    }

    private CommandResult writeNewState(GenericStateSave<Periodical> state,
                                        HttpContext context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getEntity());
                break;
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(MESSAGE_PERIODICAL_SAVE_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getEntity());
                break;
            case SUCCESS:
                context.setMessageSuccess(MESSAGE_PERIODICAL_SAVE_SUCCESS);
        }
        Long id = state.getEntity().getId();
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_PERIODICAL) +
                                      (id == null ? "" : "?" + PARAM_NAME_PERIODICAL_ID + "=" + id));
    }
}

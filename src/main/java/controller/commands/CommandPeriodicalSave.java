package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;
import services.sto.StateHolderSaveEntity;

import java.math.BigDecimal;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandPeriodicalSave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandPeriodicalSave.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderSaveEntity<Periodical> state = readState(context);
        PeriodicalService.serveSavePeriodical(state);
        return writeNewState(state, context);
    }

    private StateHolderSaveEntity<Periodical> readState(SessionRequestContent context) {
        StateHolderSaveEntity<Periodical> state = new StateHolderSaveEntity<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_ID), NULL_ID);
        state.setEntity(
                new Periodical(periodicalId == NULL_ID ? null : periodicalId,
                               NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_CATEGORY_ID), NULL_ID),
                               context.getRequestParameter(INPUT_PERIODICAL_TITLE),
                               context.getRequestParameter(INPUT_PERIODICAL_DESCRIPTION),
                               NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_MIN_PERIOD), 0),
                               NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_ISSUES), 0),
                               BigDecimal.valueOf(NumberUtils.toDouble(context.getRequestParameter(
                                       INPUT_PERIODICAL_PRICE), 0))));
        return state;
    }

    private CommandResult writeNewState(StateHolderSaveEntity<Periodical> state,
                                        SessionRequestContent context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getEntity());
                break;
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_SAVE_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getEntity());
                break;
            case SUCCESS:
                context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_SAVE_SUCCESS));
        }
        Long id = state.getEntity().getId();
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_PERIODICAL) +
                                      (id == null ? "" : "?" + PARAM_NAME_PERIODICAL_ID + "=" + id));
    }
}

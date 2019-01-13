package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.CategoryType;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;
import services.sto.StateHolderSaveEntity;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandCategorySave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandCategorySave.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderSaveEntity<PeriodicalCategory> state = readState(context);
        PeriodicalService.serveSaveCategory(state);
        return writeNewState(state, context);
    }

    private StateHolderSaveEntity<PeriodicalCategory> readState(SessionRequestContent context) {
        StateHolderSaveEntity<PeriodicalCategory> state = new StateHolderSaveEntity<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        long categoryId = NumberUtils.toLong(context.getRequestParameter(INPUT_CATEGORY_ID), NULL_ID);
        CategoryType type = CategoryType.findById(NumberUtils.toInt(context.getRequestParameter(INPUT_CATEGORY_TYPE),
                                                                    NULL_CATEGORY_TYPE));
        state.setEntity(new PeriodicalCategory(categoryId == NULL_ID ? null : categoryId,
                                               context.getRequestParameter(INPUT_CATEGORY_NAME),
                                               type,
                                               context.getRequestParameter(INPUT_CATEGORY_DESCRIPTION)));
        return state;
    }

    private CommandResult writeNewState(StateHolderSaveEntity<PeriodicalCategory> state,
                                        SessionRequestContent context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, state.getEntity());
                break;
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_SAVE_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, state.getEntity());
                break;
            case SUCCESS:
                context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_SAVE_SUCCESS));
        }
        Long id = state.getEntity().getId();
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG) +
                                      (id == null ? "" : "?" + PARAM_NAME_CATEGORY_ID + "=" + id));
    }
}

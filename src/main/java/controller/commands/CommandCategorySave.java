package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.CategoryType;
import model.Entity;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.GenericStateSave;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandCategorySave implements Command {
    private static final int NULL_CATEGORY_TYPE = -1;

    @Override
    public CommandResult execute(HttpContext context) {
        GenericStateSave<PeriodicalCategory> state = readState(context);
        ServiceFactory.getCatalogService().serveSave(state);
        return writeNewState(state, context);
    }

    private GenericStateSave<PeriodicalCategory> readState(HttpContext context) {
        GenericStateSave<PeriodicalCategory> state = new GenericStateSave<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        long categoryId = NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_CATEGORY_ID), Entity.NULL_ID);
        CategoryType type = CategoryType.findById(NumberUtils.toInt(context.getRequestParameter(INPUT_CATEGORY_TYPE),
                                                                    NULL_CATEGORY_TYPE));
        state.setEntity(new PeriodicalCategory(categoryId == Entity.NULL_ID ? null : categoryId,
                                               context.getRequestParameter(INPUT_CATEGORY_NAME),
                                               type,
                                               context.getRequestParameter(INPUT_CATEGORY_DESCRIPTION)));
        return state;
    }

    private CommandResult writeNewState(GenericStateSave<PeriodicalCategory> state,
                                        HttpContext context) {
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, state.getEntity());
                break;
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(MESSAGE_CATEGORY_SAVE_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, state.getEntity());
                break;
            case SUCCESS:
                context.setMessageSuccess(MESSAGE_CATEGORY_SAVE_SUCCESS);
        }
        Long id = state.getEntity().getId();
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG) +
                                      (id == null ? "" : "?" + PARAM_NAME_CATEGORY_ID + "=" + id));
    }
}

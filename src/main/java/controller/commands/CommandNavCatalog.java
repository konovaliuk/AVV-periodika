package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.PeriodicalCategory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;
import services.StateHolderNavCatalog;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandNavCatalog implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavCatalog.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderNavCatalog state = readState(context);
        PeriodicalService.serveNavCatalog(state);
        return writeNewState(state, context);
    }

    private StateHolderNavCatalog readState(SessionRequestContent context) {
        StateHolderNavCatalog state = new StateHolderNavCatalog();
        state.setTempCategory((PeriodicalCategory) context.getSessionAttribute(ATTR_NAME_TEMP_CATEGORY));
        context.removeSessionAttribute(ATTR_NAME_TEMP_CATEGORY);
        state.setCategoryId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), NULL_ID));
        state.setCurrentPage(NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_PAGE), 1));
        state.setItemsPerPage(NumberUtils.toInt(StringUtils.defaultIfEmpty(
                context.getRequestParameter(ATTR_NAME_ITEMS_PER_PAGE),
                (String) context.getSessionAttribute(ATTR_NAME_ITEMS_PER_PAGE)),
                                                RM_VIEW_PAGES.getInt(DEFAULT_ITEMS_PER_PAGE)));
        return state;
    }

    private CommandResult writeNewState(StateHolderNavCatalog state, SessionRequestContent context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_COMMAND_EXECUTION_ERROR));
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_WRONG_PARAMETERS));
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            case VIEW_BY_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_CATEGORY, state.getCategory());
                context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, state.getTempCategory());
            default:
                context.setRequestAttribute(ATTR_NAME_PERIODICALS, state.getPeriodicals());
                context.setRequestAttribute(ATTR_NAME_PAGE_COUNT, state.getPageCount());
                context.setRequestAttribute(ATTR_NAME_CURRENT_PAGE, state.getCurrentPage());
                context.setSessionAttribute(ATTR_NAME_ITEMS_PER_PAGE, String.valueOf(state.getItemsPerPage()));
                context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, state.getCategoryNewspapers());
                context.setRequestAttribute(ATTR_NAME_MAGAZINES, state.getCategoryMagazines());
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_CATALOG));
        }
    }
}

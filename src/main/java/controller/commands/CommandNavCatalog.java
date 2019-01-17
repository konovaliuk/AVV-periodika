package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.PeriodicalCategory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.StateNavCatalog;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandNavCatalog implements Command {
    private static final String PARAM_NAME_PAGE = "page";
    private static final String ATTR_NAME_PAGE_COUNT = "page_count";
    private static final String ATTR_NAME_CURRENT_PAGE = "current_page";
    private static final String ATTR_NAME_ITEMS_PER_PAGE = "items_per_page";
    private static final String ATTR_NAME_CATEGORY = "category_info";
    private static final String ATTR_NAME_PERIODICALS = "periodicals";

    @Override
    public CommandResult execute(HttpContext context) {
        StateNavCatalog state = readState(context);
        ServiceFactory.getCatalogService().serveNavigate(state);
        return writeNewState(state, context);
    }

    private StateNavCatalog readState(HttpContext context) {
        StateNavCatalog state = new StateNavCatalog();
        state.setTempEntity((PeriodicalCategory) context.getSessionAttribute(ATTR_NAME_TEMP_CATEGORY));
        context.removeSessionAttribute(ATTR_NAME_TEMP_CATEGORY);
        state.setEntityId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), Entity.NULL_ID));
        state.setCurrentPage(NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_PAGE), 1));
        state.setItemsPerPage(NumberUtils.toInt(StringUtils.defaultIfEmpty(
                context.getRequestParameter(ATTR_NAME_ITEMS_PER_PAGE),
                (String) context.getSessionAttribute(ATTR_NAME_ITEMS_PER_PAGE)),
                                                RM_VIEW_PAGES.getInt(DEFAULT_ITEMS_PER_PAGE)));
        return state;
    }

    private CommandResult writeNewState(StateNavCatalog state, HttpContext context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(MESSAGE_COMMAND_EXECUTION_ERROR);
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                context.setMessageDanger(MESSAGE_WRONG_PARAMETERS);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            case VIEW_BY_CATEGORY:
                context.setRequestAttribute(ATTR_NAME_CATEGORY, state.getEntity());
                context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, state.getTempEntity());
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

package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;
import services.states.StateNavPeriodical;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static common.ResourceManager.MESSAGE_COMMAND_EXECUTION_ERROR;
import static common.ResourceManager.PAGE_PERIODICAL;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CATALOG;
import static common.ViewConstants.*;

public class CommandNavPeriodical implements Command {
    private static final String ATTR_NAME_PERIODICAL = "periodical_info";
    private static final String ATTR_NAME_CATEGORIES = "categories";

    @Override
    public CommandResult execute(HttpContext context) {
        StateNavPeriodical state = readState(context);
        ServiceFactory.getPeriodicalService().serveNavigate(state);
        return writeNewState(state, context);
    }

    private StateNavPeriodical readState(HttpContext context) {
        StateNavPeriodical state = new StateNavPeriodical();
        state.setTempEntity((Periodical) context.getSessionAttribute(ATTR_NAME_TEMP_PERIODICAL));
        context.removeSessionAttribute(ATTR_NAME_TEMP_PERIODICAL);
        state.setEntityId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID),
                                             Entity.NULL_ID));
        return state;
    }

    private CommandResult writeNewState(StateNavPeriodical state, HttpContext context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(MESSAGE_COMMAND_EXECUTION_ERROR);
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            case VIEW_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_PERIODICAL, state.getEntity());
                context.setRequestAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getTempEntity());
            default:
                context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, state.getCategoryNewspapers());
                context.setRequestAttribute(ATTR_NAME_MAGAZINES, state.getCategoryMagazines());
                context.setRequestAttribute(ATTR_NAME_CATEGORIES,
                                            Stream.concat(state.getCategoryNewspapers().stream(),
                                                          state.getCategoryMagazines()
                                                                  .stream()).collect(Collectors.toList()));
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_PERIODICAL));
        }
    }
}

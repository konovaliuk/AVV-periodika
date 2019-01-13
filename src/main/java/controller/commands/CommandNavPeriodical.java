package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;
import services.sto.StateHolderNavPeriodical;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandNavPeriodical implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavPeriodical.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderNavPeriodical state = readState(context);
        PeriodicalService.serveNavPeriodical(state);
        return writeNewState(state, context);
    }

    private StateHolderNavPeriodical readState(SessionRequestContent context) {
        StateHolderNavPeriodical state = new StateHolderNavPeriodical();
        state.setTempPeriodical((Periodical) context.getSessionAttribute(ATTR_NAME_TEMP_PERIODICAL));
        context.removeSessionAttribute(ATTR_NAME_TEMP_PERIODICAL);
        state.setPeriodicalId(NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID), NULL_ID));
        return state;
    }

    private CommandResult writeNewState(StateHolderNavPeriodical state, SessionRequestContent context) {
        switch (state.getResultState()) {
            case ERROR_SERVICE_EXCEPTION:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_COMMAND_EXECUTION_ERROR));
                return CommandResult.redirect(null);
            case ERROR_WRONG_PARAMETERS:
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            case NEW_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            case EDIT_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            case VIEW_PERIODICAL:
                context.setRequestAttribute(ATTR_NAME_PERIODICAL, state.getPeriodical());
                context.setRequestAttribute(ATTR_NAME_TEMP_PERIODICAL, state.getTempPeriodical());
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

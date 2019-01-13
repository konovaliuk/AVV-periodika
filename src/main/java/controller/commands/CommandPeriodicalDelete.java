package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import static common.ResourceManager.*;
import static common.ViewConstants.PARAM_NAME_CATEGORY_ID;
import static common.ViewConstants.PARAM_NAME_PERIODICAL_ID;


public class CommandPeriodicalDelete implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandPeriodicalDelete.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID), NULL_ID);
        long categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), NULL_ID);
        if (periodicalId == NULL_ID) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_WRONG_PARAMETERS));
            return CommandResult.redirect(null);
        }
        if (!PeriodicalService.serveDeletePeriodical(periodicalId)) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_DELETE_ERROR));
            return CommandResult.redirect(
                    RM_VIEW_PAGES.get(URL_PERIODICAL) + "?" + PARAM_NAME_PERIODICAL_ID + "=" + periodicalId);
        }
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_DELETE_SUCCESS));
        if (categoryId != NULL_ID) {
            return CommandResult.redirect(
                    RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_CATEGORY_ID + "=" + categoryId);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }
}

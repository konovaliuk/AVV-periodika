package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;

import static common.ResourceManager.*;
import static common.ViewConstants.PARAM_NAME_CATEGORY_ID;
import static common.ViewConstants.PARAM_NAME_PERIODICAL_ID;

public class CommandPeriodicalDelete implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID), Entity.NULL_ID);
        long categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), Entity.NULL_ID);
        if (periodicalId == Entity.NULL_ID) {
            context.setMessageWarning(MESSAGE_WRONG_PARAMETERS);
            return CommandResult.redirect(null);
        }
        if (!ServiceFactory.getPeriodicalService().serveDelete(periodicalId)) {
            context.setMessageDanger(MESSAGE_PERIODICAL_DELETE_ERROR);
            return CommandResult.redirect(
                    RM_VIEW_PAGES.get(URL_PERIODICAL) + "?" + PARAM_NAME_PERIODICAL_ID + "=" + periodicalId);
        }
        context.setMessageSuccess(MESSAGE_PERIODICAL_DELETE_SUCCESS);
        if (categoryId != Entity.NULL_ID) {
            return CommandResult.redirect(
                    RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_CATEGORY_ID + "=" + categoryId);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }
}

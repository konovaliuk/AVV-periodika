package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import static common.ResourceManager.*;
import static common.ViewConstants.NULL_ID;
import static common.ViewConstants.PARAM_NAME_CATEGORY_ID;


public class CommandCategoryDelete implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandCategoryDelete.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        //todo match user rights
        long categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), NULL_ID);
        if (categoryId == NULL_ID) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_WRONG_PARAMETERS));
            return CommandResult.redirect(null);
        }
        if (!PeriodicalService.deleteCategory(categoryId)) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_DELETE_ERROR));
            return CommandResult
                    .redirect(RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_CATEGORY_ID + "=" + categoryId);
        }
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_DELETE_SUCCESS));
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }
}

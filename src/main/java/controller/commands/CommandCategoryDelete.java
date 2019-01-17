package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;

import static common.ResourceManager.*;
import static common.ViewConstants.PARAM_NAME_CATEGORY_ID;


public class CommandCategoryDelete implements Command {

    @Override
    public CommandResult execute(HttpContext context) {
        long categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), Entity.NULL_ID);
        if (categoryId == Entity.NULL_ID) {
            context.setMessageWarning(MESSAGE_WRONG_PARAMETERS);
            return CommandResult.redirect(null);
        }
        if (!ServiceFactory.getCatalogService().serveDelete(categoryId)) {
            context.setMessageDanger(MESSAGE_CATEGORY_DELETE_ERROR);
            return CommandResult
                    .redirect(RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_CATEGORY_ID + "=" + categoryId);
        }
        context.setMessageSuccess(MESSAGE_CATEGORY_DELETE_SUCCESS);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }
}

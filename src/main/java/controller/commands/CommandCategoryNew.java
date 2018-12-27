package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CATALOG;
import static common.ViewConstants.*;

public class CommandCategoryNew implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandCategoryNew.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        //todo match user rights
        PeriodicalCategory category = initEntity(context);
        context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, category);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_NEW_MODE + "=1");
    }

    private PeriodicalCategory initEntity(SessionRequestContent context) {
        PeriodicalCategory category = new PeriodicalCategory();
        int categoryType = NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_CATEGORY_TYPE), NEW_CATEGORY_TYPE);
        category.setType(categoryType);
        category.setName(NEW_CATEGORY_NAME);
        return category;
    }
}

package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.CategoryType;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CATALOG;
import static common.ViewConstants.ATTR_NAME_TEMP_CATEGORY;

public class CommandCategoryNew implements Command {
    private static final String PARAM_NAME_CATEGORY_TYPE = "categoryType";
    private static final CategoryType NEW_CATEGORY_TYPE = CategoryType.NEWSPAPER;
    private static final String NEW_CATEGORY_NAME = "New category";

    @Override
    public CommandResult execute(HttpContext context) {
        PeriodicalCategory category = initEntity(context);
        context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, category);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private PeriodicalCategory initEntity(HttpContext context) {
        PeriodicalCategory category = new PeriodicalCategory();
        category.setType(CategoryType.findById(NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_CATEGORY_TYPE),
                                                                 NEW_CATEGORY_TYPE.getId())));
        category.setName(NEW_CATEGORY_NAME);
        return category;
    }
}

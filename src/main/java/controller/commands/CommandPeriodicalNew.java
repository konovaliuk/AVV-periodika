package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.Entity;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import services.ServiceFactory;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_PERIODICAL;
import static common.ViewConstants.ATTR_NAME_TEMP_PERIODICAL;
import static common.ViewConstants.PARAM_NAME_CATEGORY_ID;

public class CommandPeriodicalNew implements Command {
    private static final String NEW_PERIODICAL_TITLE = "New periodical";
    private static final int NEW_PERIODICAL_ISSUES = 1;
    private static final int NEW_PERIODICAL_MIN_PERIOD = 1;

    @Override
    public CommandResult execute(HttpContext context) {
        Periodical periodical = initEntity(context);
        context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, periodical);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_PERIODICAL));
    }

    private Periodical initEntity(HttpContext context) {
        Periodical periodical = new Periodical();
        long categoryId =
                NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), Entity.NULL_ID);
        if (categoryId != Entity.NULL_ID) {
            PeriodicalCategory category = ServiceFactory.getCatalogService().findCategory(categoryId);
            if (category != null) {
                periodical.setCategoryId(categoryId);
                periodical.setCategoryName(category.getName());
                periodical.setCategoryType(category.getType());
            }
        }
        periodical.setCategoryId(categoryId);
        periodical.setTitle(NEW_PERIODICAL_TITLE);
        periodical.setMinSubscriptionPeriod(NEW_PERIODICAL_MIN_PERIOD);
        periodical.setIssuesPerPeriod(NEW_PERIODICAL_ISSUES);
        return periodical;
    }
}

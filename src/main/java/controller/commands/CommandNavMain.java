package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.CategoryType;
import model.PeriodicalCategory;
import services.ServiceFactory;

import java.util.List;
import java.util.Map;

import static common.ResourceManager.PAGE_MAIN;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ViewConstants.ATTR_NAME_MAGAZINES;
import static common.ViewConstants.ATTR_NAME_NEWSPAPERS;

public class CommandNavMain implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        Map<CategoryType, List<PeriodicalCategory>> map = ServiceFactory.getCatalogService().findCategories();
        context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, map.get(CategoryType.NEWSPAPER));
        context.setRequestAttribute(ATTR_NAME_MAGAZINES, map.get(CategoryType.MAGAZINE));
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_MAIN));
    }
}

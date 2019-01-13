package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.CategoryType;
import model.PeriodicalCategory;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.util.List;
import java.util.Map;

import static common.ResourceManager.PAGE_MAIN;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ViewConstants.ATTR_NAME_MAGAZINES;
import static common.ViewConstants.ATTR_NAME_NEWSPAPERS;

public class CommandNavMain implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavMain.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        Map<CategoryType, List<PeriodicalCategory>> map = PeriodicalService.findCategories();
        context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, map.get(CategoryType.NEWSPAPER));
        context.setRequestAttribute(ATTR_NAME_MAGAZINES, map.get(CategoryType.MAGAZINE));
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_MAIN));
    }
}

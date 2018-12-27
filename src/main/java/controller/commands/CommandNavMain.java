package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.PeriodicalCategory;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.util.List;

import static common.ResourceManager.PAGE_MAIN;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ViewConstants.ATTR_NAME_MAGAZINES;
import static common.ViewConstants.ATTR_NAME_NEWSPAPERS;

public class CommandNavMain implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavMain.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        List<PeriodicalCategory> categoryNewspapers = PeriodicalService.findNewspaperCategories();
        List<PeriodicalCategory> categoryMagazines = PeriodicalService.findMagazineCategories();
        context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, categoryNewspapers);
        context.setRequestAttribute(ATTR_NAME_MAGAZINES, categoryMagazines);
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_MAIN));
    }
}

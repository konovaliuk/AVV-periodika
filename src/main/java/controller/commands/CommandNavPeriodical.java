package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.util.List;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandNavPeriodical implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavPeriodical.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_PERIODICAL_ID), NULL_ID);
        Periodical tempPeriodical = (Periodical) context.getSessionAttribute(ATTR_NAME_TEMP_PERIODICAL);
        context.removeSessionAttribute(ATTR_NAME_TEMP_PERIODICAL);
        if (!checkParameters(periodicalId, tempPeriodical)) {
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
        }
        if (!setAttributes(context, periodicalId, tempPeriodical)) {
            return CommandResult.redirect(null);
        }
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_PERIODICAL));
    }

    private boolean checkParameters(long periodicalId, Periodical tempPeriodical) {
        return periodicalId != NULL_ID && (periodicalId != NEW_ID && tempPeriodical == null ||
                                           periodicalId != NEW_ID && tempPeriodical.getId().equals(periodicalId) ||
                                           periodicalId == NEW_ID && tempPeriodical != null &&
                                           tempPeriodical.getId() == null);
    }

    private boolean setAttributes(SessionRequestContent context, long periodicalId, Periodical tempPeriodical) {
        Periodical periodical;
        if (periodicalId == NEW_ID) {
            periodical = tempPeriodical;
            context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
            context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
        } else {
            periodical = PeriodicalService.findPeriodical(periodicalId);
            if (periodical == null) {
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_NOT_FOUND));
                return false;
            }
            if (tempPeriodical == null) {
                tempPeriodical = periodical;
            } else {
                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            }
        }
        if (periodical.getCategoryId() != null) {
            PeriodicalCategory category = PeriodicalService.findCategory(periodical.getCategoryId());
            if (category == null) {
                context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_NOT_FOUND));
                return false;
            }
            context.setRequestAttribute(ATTR_NAME_CATEGORY, category);
        }
        List<PeriodicalCategory> categories = PeriodicalService.findAllCategories();
        List<PeriodicalCategory> categoryMagazines = PeriodicalService.findMagazineCategories();
        List<PeriodicalCategory> categoryNewspapers = PeriodicalService.findNewspaperCategories();
        context.setRequestAttribute(ATTR_NAME_CATEGORIES, categories);
        context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, categoryNewspapers);
        context.setRequestAttribute(ATTR_NAME_MAGAZINES, categoryMagazines);
        context.setRequestAttribute(ATTR_NAME_PERIODICAL, periodical);
        context.setRequestAttribute(ATTR_NAME_TEMP_PERIODICAL, tempPeriodical);
        return true;
    }
}

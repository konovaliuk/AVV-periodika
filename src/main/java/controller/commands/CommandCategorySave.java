package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import controller.validation.CategoryMatching;
import controller.validation.Matcher;
import model.PeriodicalCategory;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.util.Map;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandCategorySave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandCategorySave.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        //todo match user rights
        PeriodicalCategory tempCategory = fillEntity(context);
        boolean isNew = (tempCategory.getId() == null);
        String returnURL = RM_VIEW_PAGES.get(URL_CATALOG) +
                           (isNew ? "" : "?" + PARAM_NAME_CATEGORY_ID + "=" + tempCategory.getId());
        if (!validateEntity(tempCategory, context)) {
            return CommandResult.redirect(returnURL);
        }
        PeriodicalCategory category = PeriodicalService.saveCategory(tempCategory, isNew);
        if (category == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_SAVE_ERROR));
            context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, tempCategory);
            return CommandResult.redirect(returnURL);
        }
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_SAVE_SUCCESS));
        return CommandResult
                .redirect(RM_VIEW_PAGES.get(URL_CATALOG) + "?" + PARAM_NAME_CATEGORY_ID + "=" + category.getId());
    }

    private PeriodicalCategory fillEntity(SessionRequestContent context) {
        long categoryId = NumberUtils.toLong(context.getRequestParameter(INPUT_CATEGORY_ID), NULL_ID);
        return new PeriodicalCategory(categoryId == NULL_ID ? null : categoryId,
                                      context.getRequestParameter(INPUT_CATEGORY_NAME),
                                      NumberUtils.toInt(context.getRequestParameter(INPUT_CATEGORY_TYPE),
                                                        NULL_CATEGORY_TYPE),
                                      context.getRequestParameter(INPUT_CATEGORY_DESCRIPTION));
    }

    private boolean validateEntity(PeriodicalCategory category, SessionRequestContent context) {
        Map<String, Boolean> validationInfo;
        validationInfo =
                Matcher.match(category, new CategoryMatching((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE)));
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, validationInfo);
        if (validationInfo.containsValue(false)) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
            context.setSessionAttribute(ATTR_NAME_TEMP_CATEGORY, category);
            return false;
        }
        return true;
    }

}

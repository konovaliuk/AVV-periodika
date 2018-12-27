package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import controller.validation.Matcher;
import controller.validation.PeriodicalMatching;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.math.BigDecimal;
import java.util.Map;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandPeriodicalSave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandPeriodicalSave.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        //todo match user rights
        Periodical tempPeriodical = fillEntity(context);
        boolean isNew = (tempPeriodical.getId() == null);
        String returnURL = RM_VIEW_PAGES.get(URL_PERIODICAL) +
                           (isNew ? "" : "?" + PARAM_NAME_PERIODICAL_ID + "=" + tempPeriodical.getId());
        if (!validateEntity(tempPeriodical, context)) {
            return CommandResult.redirect(returnURL);
        }
        Periodical periodical = PeriodicalService.savePeriodical(tempPeriodical, isNew);
        if (periodical == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_SAVE_ERROR));
            context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, tempPeriodical);
            return CommandResult.redirect(returnURL);
        }
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_PERIODICAL_SAVE_SUCCESS));
        return CommandResult.redirect(
                RM_VIEW_PAGES.get(URL_PERIODICAL) + "?" + PARAM_NAME_PERIODICAL_ID + "=" + periodical.getId());
    }

    private Periodical fillEntity(SessionRequestContent context) {
        long periodicalId = NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_ID), NULL_ID);
        return new Periodical(periodicalId == NULL_ID ? null : periodicalId,
                              NumberUtils.toLong(context.getRequestParameter(INPUT_PERIODICAL_CATEGORY_ID), NULL_ID),
                              context.getRequestParameter(INPUT_PERIODICAL_TITLE),
                              context.getRequestParameter(INPUT_PERIODICAL_DESCRIPTION),
                              NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_MIN_PERIOD), 0),
                              NumberUtils.toInt(context.getRequestParameter(INPUT_PERIODICAL_ISSUES), 0),
                              BigDecimal.valueOf(NumberUtils
                                                         .toDouble(context.getRequestParameter(INPUT_PERIODICAL_PRICE),
                                                                   0)));
    }

    private boolean validateEntity(Periodical periodical, SessionRequestContent context) {
        Map<String, Boolean> validationInfo;
        validationInfo = Matcher.match(periodical, new PeriodicalMatching((String) context
                .getSessionAttribute(ATTR_NAME_LANGUAGE)));
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, validationInfo);
        if (validationInfo.containsValue(false)) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
            context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, periodical);
            return false;
        }
        return true;
    }

}

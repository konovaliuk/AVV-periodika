package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_PERIODICAL;
import static common.ViewConstants.*;

public class CommandPeriodicalNew implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandPeriodicalNew.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        //todo match user rights
        Periodical periodical = initEntity(context);
        context.setSessionAttribute(ATTR_NAME_TEMP_PERIODICAL, periodical);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_PERIODICAL));
    }

    private Periodical initEntity(SessionRequestContent context) {
        Periodical periodical = new Periodical();
        long categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), NULL_ID);
        periodical.setCategoryId(categoryId);
        periodical.setTitle(NEW_PERIODICAL_TITLE);
        periodical.setMinSubscriptionPeriod(NEW_PERIODICAL_MIN_PERIOD);
        periodical.setIssuesPerPeriod(NEW_PERIODICAL_ISSUES);
        return periodical;
    }
}

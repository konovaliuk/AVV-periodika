package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.log4j.Logger;

import static common.ViewConstants.ATTR_NAME_LANGUAGE;
import static common.ViewConstants.PARAM_NAME_LANGUAGE;

public class CommandLanguage implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandLanguage.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        context.setSessionAttribute(ATTR_NAME_LANGUAGE, context.getRequestParameter(PARAM_NAME_LANGUAGE));
        return CommandResult.redirect(context.getRequestURIWithoutContext() + context.getQueryString());
    }
}

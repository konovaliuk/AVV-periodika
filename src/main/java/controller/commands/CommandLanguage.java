package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;

import static common.ViewConstants.ATTR_NAME_LANGUAGE;

public class CommandLanguage implements Command {
    private static final String PARAM_NAME_LANGUAGE = "language";

    @Override
    public CommandResult execute(HttpContext context) {
        context.setSessionAttribute(ATTR_NAME_LANGUAGE, context.getRequestParameter(PARAM_NAME_LANGUAGE));
        return CommandResult.redirect(context.getRequestURIWithoutContext() + context.getQueryString());
    }
}

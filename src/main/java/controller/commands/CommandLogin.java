package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import controller.validation.ValidationData;
import controller.validation.Validator;
import controller.validation.impl.LoginValidating;
import model.User;
import org.apache.log4j.Logger;
import services.UserService;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandLogin implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandLogin.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        User tempUser = fillEntity(context);
        if (!validateEntity(tempUser, context)) {
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_LOGIN));
        }
        User user = UserService.serveLogin(tempUser);
        if (user == null) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_LOGIN_ERROR));
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_LOGIN));
        }
        context.setSessionAttribute(ATTR_NAME_USER, user);
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_LOGIN_WELCOME));
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private User fillEntity(SessionRequestContent context) {
        return new User(context.getRequestParameter(INPUT_USER_LOGIN),
                        context.getRequestParameter(INPUT_USER_PASSWORD));
    }

    private boolean validateEntity(User user, SessionRequestContent context) {
        ValidationData validationInfo;
        validationInfo =
                Validator.match(user, new LoginValidating((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE)));
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, validationInfo);
        if (validationInfo.containsErrors()) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
            return false;
        }
        return true;
    }
}

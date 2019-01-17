package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import controller.validation.ValidationData;
import controller.validation.Validator;
import controller.validation.impl.LoginValidating;
import model.User;
import services.ServiceFactory;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandLogin implements Command {

    @Override
    public CommandResult execute(HttpContext context) {
        User tempUser = fillEntity(context);
        if (!validateEntity(tempUser, context)) {
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_LOGIN));
        }
        User user = ServiceFactory.getUserService().serveLogin(tempUser);
        if (user == null) {
            context.setMessageWarning(MESSAGE_LOGIN_ERROR);
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_LOGIN));
        }
        context.setSessionAttribute(ATTR_NAME_USER, user);
        context.setMessageSuccess(MESSAGE_LOGIN_WELCOME);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private User fillEntity(HttpContext context) {
        return new User(context.getRequestParameter(INPUT_USER_LOGIN),
                        context.getRequestParameter(INPUT_USER_PASSWORD));
    }

    private boolean validateEntity(User user, HttpContext context) {
        ValidationData validationInfo;
        validationInfo =
                Validator.match(user, new LoginValidating((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE)));
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, validationInfo);
        if (validationInfo.containsErrors()) {
            context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
            return false;
        }
        return true;
    }
}

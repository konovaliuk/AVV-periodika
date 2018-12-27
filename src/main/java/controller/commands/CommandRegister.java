package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import controller.validation.Matcher;
import controller.validation.UserMatching;
import model.User;
import model.UserInfo;
import model.UserType;
import org.apache.log4j.Logger;
import services.UserService;

import java.util.Map;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandRegister implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandRegister.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        if (context.existSessionAttribute(ATTR_NAME_USER)) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_ALREADY_LOGGED_IN));
            return CommandResult.redirect(null);
        }
        User tempUser = fillEntity(context);
        if (tempUser == null) {
            return CommandResult.redirect(null);
        }
        if (!validateEntity(tempUser, context)) {
            context.setSessionAttribute(ATTR_NAME_TEMP_USER, tempUser);
            return CommandResult.redirect(RM_VIEW_PAGES.get(URL_REGISTER));
        }
        User user = UserService.registerUser(tempUser);
        if (user == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_ERROR));
            return CommandResult.redirect(null);
        }
        UserInfo userInfo = UserService.checkLogin(user);
        if (userInfo == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_LOGIN_ERROR));
            return CommandResult.redirect(null);
        }
        context.setSessionAttribute(ATTR_NAME_USER, userInfo);
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_WELCOME));
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private User fillEntity(SessionRequestContent context) {
        UserType userTypeClient = UserService.findUserTypeClient();
        if (userTypeClient == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_ERROR));
            return null;
        }
        return new User(userTypeClient.getId(),
                        context.getRequestParameter(INPUT_USER_FIRST_NAME),
                        context.getRequestParameter(INPUT_USER_MIDDLE_NAME),
                        context.getRequestParameter(INPUT_USER_LAST_NAME),
                        context.getRequestParameter(INPUT_USER_EMAIL),
                        context.getRequestParameter(INPUT_USER_ADDRESS),
                        context.getRequestParameter(INPUT_USER_PHONE),
                        context.getRequestParameter(INPUT_USER_LOGIN),
                        context.getRequestParameter(INPUT_USER_PASSWORD));
    }

    private boolean validateEntity(User user, SessionRequestContent context) {
        Map<String, Boolean> validationInfo;
        validationInfo =
                Matcher.match(user, new UserMatching((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE)));
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, validationInfo);
        if (validationInfo.containsValue(false)) {
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
            return false;
        }
        if (UserService.existUserByLogin(user.getLogin())) {
            validationInfo.put(INPUT_USER_LOGIN, false);
            context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_EXIST));
            return false;
        }
        return true;
    }
}

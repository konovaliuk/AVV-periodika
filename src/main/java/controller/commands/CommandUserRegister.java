package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.User;
import services.ServiceFactory;
import services.UserService;
import services.states.GenericStateSave;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandUserRegister implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        GenericStateSave<User> state = readState(context);
        UserService userService = ServiceFactory.getUserService();
        userService.serveSaveUser(state);
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, state.getEntity());
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_REGISTER));
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(MESSAGE_REGISTER_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, state.getEntity());
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_REGISTER));
            case SUCCESS:
        }
        User user = userService.serveLogin(state.getEntity());
        if (user == null) {
            context.setMessageDanger(MESSAGE_LOGIN_ERROR);
            return CommandResult.redirect(null);
        }
        context.setSessionAttribute(ATTR_NAME_USER, user);
        context.setMessageSuccess(MESSAGE_REGISTER_WELCOME);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private GenericStateSave<User> readState(HttpContext context) {
        GenericStateSave<User> state = new GenericStateSave<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        state.setEntity(
                new User(context.getRequestParameter(INPUT_USER_FIRST_NAME),
                         context.getRequestParameter(INPUT_USER_MIDDLE_NAME),
                         context.getRequestParameter(INPUT_USER_LAST_NAME),
                         context.getRequestParameter(INPUT_USER_EMAIL),
                         context.getRequestParameter(INPUT_USER_ADDRESS),
                         context.getRequestParameter(INPUT_USER_PHONE),
                         context.getRequestParameter(INPUT_USER_LOGIN),
                         context.getRequestParameter(INPUT_USER_PASSWORD)));
        return state;
    }
}

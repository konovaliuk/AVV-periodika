package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.User;
import org.apache.log4j.Logger;
import services.UserService;
import services.sto.StateHolderSaveEntity;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandUserRegister implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandUserRegister.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderSaveEntity<User> state = readState(context);
        UserService.serveSaveUser(state);
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, state.getEntity());
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_REGISTER));
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, state.getEntity());
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_REGISTER));
            case SUCCESS:
        }
        User user = UserService.serveLogin(state.getEntity());
        if (user == null) {
            context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_LOGIN_ERROR));
            return CommandResult.redirect(null);
        }
        context.setSessionAttribute(ATTR_NAME_USER, user);
        context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_REGISTER_WELCOME));
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
    }

    private StateHolderSaveEntity<User> readState(SessionRequestContent context) {
        StateHolderSaveEntity<User> state = new StateHolderSaveEntity<>();
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

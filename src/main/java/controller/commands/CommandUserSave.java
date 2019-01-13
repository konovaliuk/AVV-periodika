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

public class CommandUserSave implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandUserSave.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderSaveEntity<User> state = readState(context);
        UserService.serveSaveUser(state);
        User user = state.getEntity();
        if (user != null) {
            user.setPassword(null);
        }
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(RM_VIEW_MESSAGES.get(MESSAGE_VALIDATION_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, user);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_USER_SAVE_ERROR));
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, user);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case SUCCESS:
                context.setMessageSuccess(RM_VIEW_MESSAGES.get(MESSAGE_USER_SAVE_SUCCESS));
                context.setSessionAttribute(ATTR_NAME_USER, user);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
    }

    private StateHolderSaveEntity<User> readState(SessionRequestContent context) {
        StateHolderSaveEntity<User> state = new StateHolderSaveEntity<>();
        state.setLanguage((String) context.getSessionAttribute(ATTR_NAME_LANGUAGE));
        User user = context.getCurrentUser();
        state.setEntity(
                new User(user.getId(),
                         user.getUserTypeId(),
                         context.getRequestParameter(INPUT_USER_FIRST_NAME),
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

package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.User;
import services.ServiceFactory;
import services.states.GenericStateSave;

import static common.ResourceManager.*;
import static common.ViewConstants.*;

public class CommandUserSave implements Command {
    @Override
    public CommandResult execute(HttpContext context) {
        GenericStateSave<User> state = readState(context);
        ServiceFactory.getUserService().serveSaveUser(state);
        User user = state.getEntity();
        if (user != null) {
            user.setPassword(null);
        }
        context.setSessionAttribute(ATTR_NAME_VALIDATION_INFO, state.getValidationInfo());
        switch (state.getResultState()) {
            case ERROR_WRONG_PARAMETERS:
                context.setMessageWarning(MESSAGE_VALIDATION_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, user);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case ERROR_ENTITY_NOT_SAVED:
                context.setMessageDanger(MESSAGE_USER_SAVE_ERROR);
                context.setSessionAttribute(ATTR_NAME_TEMP_USER, user);
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
            case SUCCESS:
                context.setMessageSuccess(MESSAGE_USER_SAVE_SUCCESS);
                context.setSessionAttribute(ATTR_NAME_USER, user);
        }
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CABINET));
    }

    private GenericStateSave<User> readState(HttpContext context) {
        GenericStateSave<User> state = new GenericStateSave<>();
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

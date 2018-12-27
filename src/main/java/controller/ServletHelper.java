package controller;

import common.LoggerLoader;
import common.ViewConstants;
import controller.commands.*;
import org.apache.log4j.Logger;

import static common.ResourceManager.*;

public class ServletHelper {
    private static final Logger LOGGER = LoggerLoader.getLogger(ServletHelper.class);

    public static Command findCommand(SessionRequestContent context) {
        String commandName = context.getRequestParameter(ViewConstants.PARAM_NAME_COMMAND);
        LOGGER.info("Requested command: " + commandName);
        if (commandName != null && !commandName.isEmpty()) {
            try {
                return CommandsEnum.valueOf(commandName.toUpperCase()).getCommand();
            } catch (IllegalArgumentException e) {
                LOGGER.error("Incorrect command: " + commandName);
                return CommandsEnum.EMPTY.getCommand();
            }
        }
        String path = context.getServletPath();
        if (path == null || path.isEmpty()) {
            return CommandsEnum.NAV_JSP.getCommand();
        }
        if (path.equals(RM_VIEW_PAGES.get(URL_ROOT)) || path.equals(RM_VIEW_PAGES.get(URL_MAIN))) {
            return CommandsEnum.NAV_MAIN.getCommand();
        }
        if (path.equals(RM_VIEW_PAGES.get(URL_CATALOG))) {
            return CommandsEnum.NAV_CATALOG.getCommand();
        }
        if (path.equals(RM_VIEW_PAGES.get(URL_PERIODICAL))) {
            return CommandsEnum.NAV_PERIODICAL.getCommand();
        }
        return CommandsEnum.NAV_JSP.getCommand();
    }

    private enum CommandsEnum {
        EMPTY(new CommandEmpty()),
        NAV_JSP(new CommandNavJSP()),
        NAV_MAIN(new CommandNavMain()),
        NAV_CATALOG(new CommandNavCatalog()),
        NAV_PERIODICAL(new CommandNavPeriodical()),
        LOGIN(new CommandLogin()),
        LOGOUT(new CommandLogout()),
        REGISTER(new CommandRegister()),
        PERIODICAL_SAVE(new CommandPeriodicalSave()),
        PERIODICAL_NEW(new CommandPeriodicalNew()),
        PERIODICAL_DELETE(new CommandPeriodicalDelete()),
        CATEGORY_SAVE(new CommandCategorySave()),
        CATEGORY_NEW(new CommandCategoryNew()),
        CATEGORY_DELETE(new CommandCategoryDelete());
        private Command command;

        CommandsEnum(Command command) {
            this.command = command;
        }

        public Command getCommand() {
            return command;
        }
    }
}
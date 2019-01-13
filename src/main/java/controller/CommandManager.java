package controller;

import common.LoggerLoader;
import common.ViewConstants;
import org.apache.log4j.Logger;

import static common.ResourceManager.*;

public class CommandManager {
    public static final String PATH_MAIN = RM_VIEW_PAGES.get(URL_MAIN);
    public static final String PATH_LOGOUT = RM_VIEW_PAGES.get(URL_LOGOUT);
    public static final String PATH_CATALOG = RM_VIEW_PAGES.get(URL_CATALOG);
    public static final String PATH_PERIODICAL = RM_VIEW_PAGES.get(URL_PERIODICAL);
    public static final String PATH_CABINET = RM_VIEW_PAGES.get(URL_CABINET);
    public static final String PATH_SUBSCRIPTION = RM_VIEW_PAGES.get(URL_SUBSCRIPTION);
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandManager.class);
    private static final String PATH_ROOT = RM_VIEW_PAGES.get(URL_ROOT);

    public static Command findCommand(SessionRequestContent context) {
        CommandEnum command = CommandEnum.EMPTY;
        if ("POST".equals(context.getMethod())) {
            String commandName = context.getRequestParameter(ViewConstants.PARAM_NAME_COMMAND);
            if (commandName != null && !commandName.isEmpty()) {
                try {
                    command = CommandEnum.valueOf(commandName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    LOGGER.error("Incorrect command: " + commandName);
                }
            }
        }
        if (command == CommandEnum.EMPTY) {
            String path = context.getServletPath();
            if (path != null && !path.isEmpty()) {
                command = CommandEnum.NAV_JSP;
                if (path.equals(PATH_ROOT) || path.equals(PATH_MAIN)) {
                    command = CommandEnum.NAV_MAIN;
                } else if (path.equals(PATH_LOGOUT)) {
                    command = CommandEnum.LOGOUT;
                } else if (path.equals(PATH_CATALOG)) {
                    command = CommandEnum.NAV_CATALOG;
                } else if (path.equals(PATH_PERIODICAL)) {
                    command = CommandEnum.NAV_PERIODICAL;
                } else if (path.equals(PATH_CABINET)) {
                    command = CommandEnum.NAV_CABINET;
                } else if (path.equals(PATH_SUBSCRIPTION)) {
                    command = CommandEnum.NAV_SUBSCRIPTION;
                }
            }
        }
        return CommandSecurity.validateCommandRequest(context, command);
    }
}
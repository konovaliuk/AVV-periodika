package controller;

import common.LoggerLoader;
import common.ViewConstants;
import org.apache.log4j.Logger;

import static common.ResourceManager.*;

public class ServletHelper {
    private static final Logger LOGGER = LoggerLoader.getLogger(ServletHelper.class);

    public static Command findCommand(SessionRequestContent context) {
        CommandsEnum command = CommandsEnum.EMPTY;
        if ("POST".equals(context.getMethod())) {
            String commandName = context.getRequestParameter(ViewConstants.PARAM_NAME_COMMAND);
            LOGGER.info("Requested command: " + commandName);
            if (commandName != null && !commandName.isEmpty()) {
                try {
                    command = CommandsEnum.valueOf(commandName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    LOGGER.error("Incorrect command: " + commandName);
                }
            }
        }
        if (command == CommandsEnum.EMPTY) {
            String path = context.getServletPath();
            if (path != null && !path.isEmpty()) {
                command = CommandsEnum.NAV_JSP;
                if (path.equals(RM_VIEW_PAGES.get(URL_ROOT)) || path.equals(RM_VIEW_PAGES.get(URL_MAIN))) {
                    command = CommandsEnum.NAV_MAIN;
                } else if (path.equals(RM_VIEW_PAGES.get(URL_LOGOUT))) {
                    command = CommandsEnum.LOGOUT;
                } else if (path.equals(RM_VIEW_PAGES.get(URL_CATALOG))) {
                    command = CommandsEnum.NAV_CATALOG;
                } else if (path.equals(RM_VIEW_PAGES.get(URL_PERIODICAL))) {
                    command = CommandsEnum.NAV_PERIODICAL;
                }
            }
        }
        return SecurityFilter.validateCommandRequest(context, command);
    }
}
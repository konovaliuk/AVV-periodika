package controller;

import common.LoggerLoader;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static common.ResourceManager.*;

public class CommandManager {
    private static final Map<String, CommandEnum> PATH_COMMAND_MAP;
    private static final String PARAM_NAME_COMMAND = "command";
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandManager.class);
    private static final String LOG_INCORRECT_COMMAND = "Incorrect command: ";
    private static final String METHOD_POST = "POST";

    static {
        Map<String, CommandEnum> aMap = new HashMap<>();
        aMap.put(null, CommandEnum.EMPTY);
        aMap.put("", CommandEnum.EMPTY);
        aMap.put(RM_VIEW_PAGES.get(URL_ROOT), CommandEnum.NAV_MAIN);
        aMap.put(RM_VIEW_PAGES.get(URL_MAIN), CommandEnum.NAV_MAIN);
        aMap.put(RM_VIEW_PAGES.get(URL_LOGOUT), CommandEnum.LOGOUT);
        aMap.put(RM_VIEW_PAGES.get(URL_CATALOG), CommandEnum.NAV_CATALOG);
        aMap.put(RM_VIEW_PAGES.get(URL_PERIODICAL), CommandEnum.NAV_PERIODICAL);
        aMap.put(RM_VIEW_PAGES.get(URL_CABINET), CommandEnum.NAV_CABINET);
        aMap.put(RM_VIEW_PAGES.get(URL_SUBSCRIPTION), CommandEnum.NAV_SUBSCRIPTION);
        aMap.put(RM_VIEW_PAGES.get(URL_PAYMENT), CommandEnum.NAV_PAYMENT);
        PATH_COMMAND_MAP = Collections.unmodifiableMap(aMap);
    }

    public static Command findCommand(HttpContext context) {
        CommandEnum command = CommandEnum.EMPTY;
        if (METHOD_POST.equals(context.getMethod())) {
            String commandName = context.getRequestParameter(PARAM_NAME_COMMAND);
            if (commandName != null && !commandName.isEmpty()) {
                try {
                    command = CommandEnum.valueOf(commandName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    LOGGER.error(LOG_INCORRECT_COMMAND + commandName);
                }
            }
        }
        if (command == CommandEnum.EMPTY) {
            String path = context.getRequestURIWithoutContext().toLowerCase();
            command = PATH_COMMAND_MAP.getOrDefault(path, CommandEnum.NAV_JSP);
        }
        return CommandSecurity.validateCommandRequest(context, command);
    }
}
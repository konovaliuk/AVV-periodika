package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static common.ResourceManager.*;

public class CommandNavJSP implements Command {
    private static final String LOG_INCORRECT_NAVIGATION_PATH = "Incorrect navigation path: ";
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavJSP.class);
    private static final Map<String, String> PATH_PAGE_MAP;

    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put(RM_VIEW_PAGES.get(URL_LOGIN), RM_VIEW_PAGES.get(PAGE_LOGIN));
        aMap.put(RM_VIEW_PAGES.get(URL_REGISTER), RM_VIEW_PAGES.get(PAGE_REGISTER));
        PATH_PAGE_MAP = Collections.unmodifiableMap(aMap);
    }

    @Override
    public CommandResult execute(HttpContext context) {
        String path = context.getRequestURIWithoutContext().toLowerCase();
        String page = PATH_PAGE_MAP.get(context.getRequestURIWithoutContext().toLowerCase());
        if (page != null) {
            return CommandResult.forward(page);
        }
        LOGGER.error(LOG_INCORRECT_NAVIGATION_PATH + path);
        return CommandResult.redirect(null);
    }
}

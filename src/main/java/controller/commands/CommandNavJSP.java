package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.log4j.Logger;

import static common.ResourceManager.*;

public class CommandNavJSP implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavJSP.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        String path = context.getServletPath();
        LOGGER.info("Requested path: " + path);
        if (path != null && !path.isEmpty()) {
            if (path.equals(RM_VIEW_PAGES.get(URL_LOGIN))) {
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_LOGIN));
            }
            if (path.equals(RM_VIEW_PAGES.get(URL_REGISTER))) {
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_REGISTER));
            }
        }
        LOGGER.error("Incorrect navigation path: " + path);
        return CommandResult.redirect(null);
    }
}

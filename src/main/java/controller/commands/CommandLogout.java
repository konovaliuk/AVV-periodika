package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.log4j.Logger;

public class CommandLogout implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandLogout.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        context.invalidateSession();
        return CommandResult.redirect(null);
    }
}

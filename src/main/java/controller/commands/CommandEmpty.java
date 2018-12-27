package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import org.apache.log4j.Logger;

import static common.ResourceManager.MESSAGE_WRONG_COMMAND;
import static common.ResourceManager.RM_VIEW_MESSAGES;

public class CommandEmpty implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandEmpty.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_WRONG_COMMAND));
        return CommandResult.redirect(null);
    }
}

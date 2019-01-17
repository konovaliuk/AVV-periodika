package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;

import static common.ResourceManager.MESSAGE_WRONG_COMMAND;

public class CommandEmpty implements Command {

    @Override
    public CommandResult execute(HttpContext context) {
        context.setMessageDanger(MESSAGE_WRONG_COMMAND);
        return CommandResult.redirect(null);
    }
}

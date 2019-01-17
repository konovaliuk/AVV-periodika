package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;

public class CommandLogout implements Command {

    @Override
    public CommandResult execute(HttpContext context) {
        context.invalidateSession();
        return CommandResult.redirect(null);
    }
}

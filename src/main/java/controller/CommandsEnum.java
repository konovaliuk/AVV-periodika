package controller;

import controller.commands.*;

public enum CommandsEnum {
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

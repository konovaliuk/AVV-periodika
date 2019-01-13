package controller;

import controller.commands.*;

public enum CommandEnum {
    EMPTY(new CommandEmpty()),
    LANGUAGE(new CommandLanguage()),
    NAV_JSP(new CommandNavJSP()),
    NAV_MAIN(new CommandNavMain()),
    NAV_CATALOG(new CommandNavCatalog()),
    NAV_PERIODICAL(new CommandNavPeriodical()),
    NAV_CABINET(new CommandNavCabinet()),
    NAV_SUBSCRIPTION(new CommandNavSubscription()),
    LOGIN(new CommandLogin()),
    LOGOUT(new CommandLogout()),
    REGISTER(new CommandUserRegister()),
    USER_SAVE(new CommandUserSave()),
    PERIODICAL_SAVE(new CommandPeriodicalSave()),
    PERIODICAL_NEW(new CommandPeriodicalNew()),
    PERIODICAL_DELETE(new CommandPeriodicalDelete()),
    SUBSCRIPTION_NEW(new CommandSubscriptionNew()),
    SUBSCRIPTION_SAVE(new CommandSubscriptionSave()),
    SUBSCRIPTION_DELETE(new CommandSubscriptionDelete()),
    CATEGORY_SAVE(new CommandCategorySave()),
    CATEGORY_NEW(new CommandCategoryNew()),
    CATEGORY_DELETE(new CommandCategoryDelete());
    private Command command;

    CommandEnum(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}

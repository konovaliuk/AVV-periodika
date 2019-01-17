package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.HttpContext;
import model.SubscriptionInfo;
import model.SubscriptionStatus;
import model.User;
import services.ServiceFactory;

import java.util.List;
import java.util.Map;

import static common.ResourceManager.PAGE_CABINET;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ViewConstants.ATTR_NAME_EDIT_MODE;
import static common.ViewConstants.ATTR_NAME_TEMP_USER;

public class CommandNavCabinet implements Command {
    private static final String ATTR_NAME_SUBSCRIPTION_SAVED = "subscriptions_saved";
    private static final String ATTR_NAME_SUBSCRIPTION_ACTIVE = "subscriptions_active";
    private static final String ATTR_NAME_SUBSCRIPTION_FINISHED = "subscriptions_finished";

    @Override
    public CommandResult execute(HttpContext context) {
        User tempUser = (User) context.getSessionAttribute(ATTR_NAME_TEMP_USER);
        context.removeSessionAttribute(ATTR_NAME_TEMP_USER);
        User user = context.getCurrentUser();
        if (user == null || user.notSaved()) {
            return CommandResult.redirect(null);
        }
        Map<SubscriptionStatus, List<SubscriptionInfo>> map =
                ServiceFactory.getSubscriptionService().findUserSubscriptions(user.getId());
        if (tempUser != null) {
            context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
            context.setRequestAttribute(ATTR_NAME_TEMP_USER, tempUser);
        }
        context.setRequestAttribute(ATTR_NAME_SUBSCRIPTION_SAVED, map.get(SubscriptionStatus.SAVED));
        context.setRequestAttribute(ATTR_NAME_SUBSCRIPTION_ACTIVE, map.get(SubscriptionStatus.ACTIVE));
        context.setRequestAttribute(ATTR_NAME_SUBSCRIPTION_FINISHED, map.get(SubscriptionStatus.FINISHED));
        return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_CABINET));
    }
}

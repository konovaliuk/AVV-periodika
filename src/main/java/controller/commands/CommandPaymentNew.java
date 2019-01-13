package controller.commands;

import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Payment;
import services.SubscriptionService;
import services.sto.StateHolderPayment;

import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_SUBSCRIPTION;

public class CommandPaymentNew implements Command {

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolderPayment state = new StateHolderPayment();
        state.setEntity(new Payment());
        //getting list of selected subs. from request
        SubscriptionService.serveNewPayment(state);
        return CommandResult.redirect(RM_VIEW_PAGES.get(URL_SUBSCRIPTION));
    }
}

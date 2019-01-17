package services;

import common.LoggerLoader;
import controller.validation.ValidationData;
import controller.validation.Validator;
import controller.validation.impl.SubscriptionValidating;
import model.Payment;
import model.Subscription;
import model.SubscriptionInfo;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.SubscriptionDAO;
import persistence.dao.SubscriptionInfoDAO;
import persistence.database.DbContext;
import services.states.GenericStateSave;
import services.states.StateSavePayment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static common.ResourceManager.MESSAGE_SUBSCRIPTION_WRONG;
import static model.Entity.NULL_ID;

public class PaymentService {
    private static final Logger LOGGER = LoggerLoader.getLogger(PaymentService.class);

    PaymentService() {
    }

    public void serveNewPayment(StateSavePayment state) {
        String[] subscriptionIds = state.getSubscriptionIds();
        if (subscriptionIds == null || subscriptionIds.length == 0) {
            state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        List<SubscriptionInfo> subscriptions = new ArrayList<>();
        ValidationData validationInfo = new ValidationData();
        SubscriptionValidating validating = new SubscriptionValidating(state.getLanguage());
        try (DbContext dbContext = new DbContext()) {
            SubscriptionInfoDAO subscriptionInfoDAO = DAOFactory.getFactory().getSubscriptionInfoDAO();
            SubscriptionInfo subscription;
            for (String s : subscriptionIds) {
                long id = NumberUtils.toLong(s, NULL_ID);
                if (id == NULL_ID) {
                    state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                subscription = subscriptionInfoDAO.findById(id, dbContext);
                if (subscription == null || !subscription.getUserId().equals(state.getUser().getId())) {
                    state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                subscription.fillCalculatedFields();
                subscriptions.add(subscription);
                if (Validator.match(subscription, validating).containsErrors()) {
                    validationInfo.setError(subscription.getId().toString(), MESSAGE_SUBSCRIPTION_WRONG);
                }
            }
        } catch (DAOException e) {
            LOGGER.error(e);
        }
        state.setValidationInfo(validationInfo);
        if (validationInfo.containsErrors()) {
            state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        state.setEntity(new Payment(Instant.now(), calcPaymentSum(subscriptions), subscriptions));
        state.setResultState(GenericStateSave.State.SUCCESS);
    }

    public void serveNavPayment(StateSavePayment state) {
        Payment payment = state.getEntity();
        try (DbContext dbContext = new DbContext()) {
            if (payment == null) {
                state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
                return;
            }
            if (payment.saved()) {
                payment = DAOFactory.getFactory().getPaymentDAO().findById(payment.getId(), dbContext);
                if (payment == null) {
                    state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                List<SubscriptionInfo> subscriptions = DAOFactory.getFactory()
                        .getSubscriptionInfoDAO().findAllByPayment(state.getUser().getId(), payment.getId(), dbContext);
                if (subscriptions.isEmpty() || !state.getUser().getId().equals(subscriptions.get(0).getUserId())) {
                    state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                payment.setSubscriptions(subscriptions);
                state.setEntity(payment);
            }
            state.setResultState(StateSavePayment.State.SUCCESS);
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
        }
    }

    public void serveSavePayment(StateSavePayment state) {
        Payment payment = state.getEntity();
        if (payment == null) {
            state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        List<SubscriptionInfo> subscriptions = payment.getSubscriptions();
        if (subscriptions == null ||
            subscriptions.isEmpty() ||
            payment.getSum().compareTo(calcPaymentSum(subscriptions)) != 0) {
            state.setResultState(StateSavePayment.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        Payment newPayment;
        try (DbContext dbContext = new DbContext()) {
            dbContext.beginTransaction();
            payment.setDate(Instant.now());
            newPayment = DAOFactory.getFactory().getPaymentDAO().create(payment, dbContext);
            if (newPayment == null || newPayment.notSaved()) {
                state.setResultState(StateSavePayment.State.ERROR_ENTITY_NOT_SAVED);
                dbContext.endTransaction(false);
                return;
            }
            SubscriptionDAO subscriptionDAO = DAOFactory.getFactory().getSubscriptionDAO();
            for (Subscription subscription : subscriptions) {
                subscription.setPaymentId(payment.getId());
                if (subscriptionDAO.update(subscription, dbContext) == null) {
                    state.setResultState(StateSavePayment.State.ERROR_ENTITY_NOT_SAVED);
                    dbContext.endTransaction(false);
                    return;
                }
            }
            dbContext.endTransaction(true);
            state.setResultState(StateSavePayment.State.SUCCESS);
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateSavePayment.State.ERROR_ENTITY_NOT_SAVED);
        }
    }

    private BigDecimal calcPaymentSum(List<SubscriptionInfo> subscriptions) {
        BigDecimal result = BigDecimal.ZERO;
        for (SubscriptionInfo s : subscriptions) {
            result = result.add(s.getCalcSum());
        }
        return result;
    }
}

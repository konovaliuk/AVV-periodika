package services;

import common.LoggerLoader;
import controller.validation.Validator;
import controller.validation.impl.SubscriptionValidating;
import model.Periodical;
import model.Subscription;
import model.SubscriptionInfo;
import model.SubscriptionStatus;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.PeriodicalDAO;
import persistence.dao.SubscriptionDAO;
import persistence.dao.SubscriptionInfoDAO;
import persistence.database.DBContext;
import services.sto.StateHolderNavSubscription;
import services.sto.StateHolderPayment;
import services.sto.StateHolderSaveEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.ResourceManager.NULL_ID;

public class SubscriptionService {
    private static final Logger LOGGER = LoggerLoader.getLogger(SubscriptionService.class);
    private static final Byte MASK_NEW_SUBSCRIPTION = 0b0010;
    private static final Byte MASK_VIEW_SUBSCRIPTION = 0b1100;
    private static final Byte MASK_EDIT_SUBSCRIPTION = 0b1001;
    private static final Map<Byte, StateHolderNavSubscription.State> SUBSCRIPTION_STATE_MAP;

    static {
        Map<Byte, StateHolderNavSubscription.State> pMap = new HashMap<>();
        pMap.put(MASK_NEW_SUBSCRIPTION, StateHolderNavSubscription.State.NEW_SUBSCRIPTION);
        pMap.put(MASK_VIEW_SUBSCRIPTION, StateHolderNavSubscription.State.VIEW_SUBSCRIPTION);
        pMap.put(MASK_EDIT_SUBSCRIPTION, StateHolderNavSubscription.State.EDIT_SUBSCRIPTION);
        SUBSCRIPTION_STATE_MAP = Collections.unmodifiableMap(pMap);
    }

    public static Map<SubscriptionStatus, List<SubscriptionInfo>> findUserSubscriptions(Long userId) {
        Map<SubscriptionStatus, List<SubscriptionInfo>> map = new HashMap<>();
        DBContext dbContext = new DBContext();

        SubscriptionInfoDAO dao = DAOFactory.getSubscriptionInfoDAO();
        map.put(SubscriptionStatus.SAVED,
                DBContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                              SubscriptionStatus.SAVED,
                                                                              context), dbContext));
        map.put(SubscriptionStatus.ACTIVE,
                DBContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                              SubscriptionStatus.ACTIVE,
                                                                              context), dbContext));
        map.put(SubscriptionStatus.FINISHED,
                DBContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                              SubscriptionStatus.FINISHED,
                                                                              context), dbContext));
        dbContext.close();
        return map;
    }

    public static void serveSaveSubscription(StateHolderSaveEntity<SubscriptionInfo> state) {
        SubscriptionInfo subscription = state.getEntity();
        SubscriptionInfo newSubscription = null;
        try (DBContext dbContext = new DBContext()) {
            SubscriptionInfoDAO subscriptionInfoDAO = DAOFactory.getSubscriptionInfoDAO();
            PeriodicalDAO periodicalDAO = DAOFactory.getPeriodicalDAO();
            Periodical periodical = periodicalDAO.findById(subscription.getPeriodicalId(), dbContext);
            subscription.setPeriodical(periodical);
            state.setValidationInfo(Validator.match(subscription, new SubscriptionValidating(state.getLanguage())));
            if (periodical == null ||
                state.getValidationInfo().containsErrors() ||
                !state.getUser().getId().equals(subscription.getUserId())) {
                state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
                return;
            }
            subscription.fillCalculatedFields();
            SubscriptionDAO subscriptionDAO = DAOFactory.getSubscriptionDAO();
            if (subscription.getId() == null) {
                newSubscription = new SubscriptionInfo(subscriptionDAO.create(subscription, dbContext), periodical);
            } else {
                if (!isSubscriptionsReplaceable(subscriptionInfoDAO.findById(subscription.getId(), dbContext),
                                                subscription)) {
                    state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                newSubscription = new SubscriptionInfo(subscriptionDAO.update(subscription, dbContext), periodical);
            }
        } catch (DAOException e) {
            LOGGER.error(e);
        }
        ServiceHelper.setSavedEntity(state, newSubscription);
    }

    public static boolean serveDeleteSubscription(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getSubscriptionDAO()
                .delete(id, context)));
    }

    public static void serveNavSubscription(StateHolderNavSubscription state) {
        Long subscriptionId = state.getSubscriptionId();
        SubscriptionInfo tempSubscription = state.getTempSubscription();
        byte mask = ServiceHelper.getStateMask(subscriptionId, tempSubscription);
        state.setResultState(SUBSCRIPTION_STATE_MAP.getOrDefault(mask,
                                                                 StateHolderNavSubscription.State.ERROR_WRONG_PARAMETERS));
        try (DBContext dbContext = new DBContext()) {
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_SUBSCRIPTION:
                    state.setSubscription(tempSubscription);
                    break;
                case VIEW_SUBSCRIPTION:
                case EDIT_SUBSCRIPTION:
                    state.setSubscription(DAOFactory.getSubscriptionInfoDAO().findById(subscriptionId, dbContext));
                    if (state.getSubscription() == null ||
                        !state.getUser().getId().equals(state.getSubscription().getUserId())) {
                        state.setResultState(StateHolderNavSubscription.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempSubscription() == null) {
                        state.setTempSubscription(state.getSubscription());
                    }
                default:
            }
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderNavSubscription.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    public static void serveNewPayment(StateHolderPayment state) {
    }

    private static boolean isSubscriptionsReplaceable(Subscription savedSubscription,
                                                      Subscription newSubscription) {
        return savedSubscription != null &&
               (savedSubscription.getPaymentId() == null ||
                savedSubscription.getPaymentId() == NULL_ID) &&
               savedSubscription.getPeriodicalId().equals(newSubscription.getPeriodicalId()) &&
               newSubscription.getUserId().equals(savedSubscription.getUserId());
    }
}

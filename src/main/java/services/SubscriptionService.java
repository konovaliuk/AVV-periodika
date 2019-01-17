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
import persistence.database.DbContext;
import services.states.GenericStateSave;
import services.states.StateNavSubscription;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Entity.NULL_ID;

public class SubscriptionService {
    private static final Logger LOGGER = LoggerLoader.getLogger(SubscriptionService.class);
    private static final Byte MASK_NEW_SUBSCRIPTION = 0b0010;
    private static final Byte MASK_VIEW_SUBSCRIPTION = 0b1100;
    private static final Byte MASK_EDIT_SUBSCRIPTION = 0b1001;
    private static final Map<Byte, StateNavSubscription.State> SUBSCRIPTION_STATE_MAP;

    static {
        Map<Byte, StateNavSubscription.State> pMap = new HashMap<>();
        pMap.put(MASK_NEW_SUBSCRIPTION, StateNavSubscription.State.NEW_SUBSCRIPTION);
        pMap.put(MASK_VIEW_SUBSCRIPTION, StateNavSubscription.State.VIEW_SUBSCRIPTION);
        pMap.put(MASK_EDIT_SUBSCRIPTION, StateNavSubscription.State.EDIT_SUBSCRIPTION);
        SUBSCRIPTION_STATE_MAP = Collections.unmodifiableMap(pMap);
    }

    SubscriptionService() {
    }

    public Map<SubscriptionStatus, List<SubscriptionInfo>> findUserSubscriptions(Long userId) {
        Map<SubscriptionStatus, List<SubscriptionInfo>> map = new HashMap<>();
        try (DbContext dbContext = new DbContext()) {

            SubscriptionInfoDAO dao = DAOFactory.getFactory().getSubscriptionInfoDAO();
            map.put(SubscriptionStatus.SAVED,
                    DbContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                                  SubscriptionStatus.SAVED,
                                                                                  context), dbContext));
            map.put(SubscriptionStatus.ACTIVE,
                    DbContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                                  SubscriptionStatus.ACTIVE,
                                                                                  context), dbContext));
            map.put(SubscriptionStatus.FINISHED,
                    DbContext.executeOrNull(context -> dao.findAllByUserAndStatus(userId,
                                                                                  SubscriptionStatus.FINISHED,
                                                                                  context), dbContext));
        }
        return map;
    }

    public void serveSaveSubscription(GenericStateSave<SubscriptionInfo> state) {
        SubscriptionInfo subscription = state.getEntity();
        SubscriptionInfo newSubscription = null;
        try (DbContext dbContext = new DbContext()) {
            SubscriptionInfoDAO subscriptionInfoDAO = DAOFactory.getFactory().getSubscriptionInfoDAO();
            PeriodicalDAO periodicalDAO = DAOFactory.getFactory().getPeriodicalDAO();
            Periodical periodical = periodicalDAO.findById(subscription.getPeriodicalId(), dbContext);
            subscription.setPeriodical(periodical);
            state.setValidationInfo(Validator.match(subscription, new SubscriptionValidating(state.getLanguage())));
            if (periodical == null ||
                state.getValidationInfo().containsErrors() ||
                !state.getUser().getId().equals(subscription.getUserId())) {
                state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
                return;
            }
            subscription.fillCalculatedFields();
            SubscriptionDAO subscriptionDAO = DAOFactory.getFactory().getSubscriptionDAO();
            if (subscription.notSaved()) {
                newSubscription = new SubscriptionInfo(subscriptionDAO.create(subscription, dbContext), periodical);
            } else {
                if (!isSubscriptionsReplaceable(subscriptionInfoDAO.findById(subscription.getId(), dbContext),
                                                subscription)) {
                    state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
                    return;
                }
                newSubscription = new SubscriptionInfo(subscriptionDAO.update(subscription, dbContext), periodical);
            }
        } catch (DAOException e) {
            LOGGER.error(e);
        }
        ServiceHelper.setSavedEntity(state, newSubscription);
    }

    public boolean serveDeleteSubscription(Long id) {
        return Boolean.TRUE.equals(DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory()
                .getSubscriptionDAO()
                .delete(id, context)));
    }

    public void serveNavSubscription(StateNavSubscription state) {
        Long subscriptionId = state.getEntityId();
        SubscriptionInfo tempSubscription = state.getTempEntity();
        byte mask = ServiceHelper.getStateMask(subscriptionId, tempSubscription);
        state.setResultState(SUBSCRIPTION_STATE_MAP.getOrDefault(mask,
                                                                 StateNavSubscription.State.ERROR_WRONG_PARAMETERS));
        try (DbContext dbContext = new DbContext()) {
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_SUBSCRIPTION:
                    state.setEntity(tempSubscription);
                    break;
                case VIEW_SUBSCRIPTION:
                case EDIT_SUBSCRIPTION:
                    state.setEntity(DAOFactory.getFactory()
                                            .getSubscriptionInfoDAO()
                                            .findById(subscriptionId, dbContext));
                    if (state.getEntity() == null ||
                        !state.getUser().getId().equals(state.getEntity().getUserId())) {
                        state.setResultState(StateNavSubscription.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempEntity() == null) {
                        state.setTempEntity(state.getEntity());
                    }
                default:
            }
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateNavSubscription.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    private boolean isSubscriptionsReplaceable(Subscription savedSubscription, Subscription newSubscription) {
        return savedSubscription != null &&
               (savedSubscription.getPaymentId() == null ||
                savedSubscription.getPaymentId() == NULL_ID) &&
               savedSubscription.getPeriodicalId().equals(newSubscription.getPeriodicalId()) &&
               newSubscription.getUserId().equals(savedSubscription.getUserId());
    }
}

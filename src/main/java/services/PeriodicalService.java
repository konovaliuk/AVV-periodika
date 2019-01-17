package services;

import common.LoggerLoader;
import controller.validation.Validator;
import controller.validation.impl.PeriodicalValidating;
import model.CategoryType;
import model.Periodical;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.PeriodicalCategoryDAO;
import persistence.database.DbContext;
import services.states.GenericStateSave;
import services.states.StateNavPeriodical;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PeriodicalService {
    private static final Logger LOGGER = LoggerLoader.getLogger(PeriodicalService.class);
    private static final Byte MASK_NEW_PERIODICAL = 0b0010;
    private static final Byte MASK_VIEW_PERIODICAL = 0b1100;
    private static final Byte MASK_EDIT_PERIODICAL = 0b1001;
    private static final Map<Byte, StateNavPeriodical.State> PERIODICAL_STATE_MAP;

    static {
        Map<Byte, StateNavPeriodical.State> pMap = new HashMap<>();
        pMap.put(MASK_NEW_PERIODICAL, StateNavPeriodical.State.NEW_PERIODICAL);
        pMap.put(MASK_VIEW_PERIODICAL, StateNavPeriodical.State.VIEW_PERIODICAL);
        pMap.put(MASK_EDIT_PERIODICAL, StateNavPeriodical.State.EDIT_PERIODICAL);
        PERIODICAL_STATE_MAP = Collections.unmodifiableMap(pMap);
    }

    public Periodical findPeriodical(Long id) {
        return DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalDAO()
                .findById(id, context));
    }

    public boolean serveDelete(Long id) {
        return Boolean.TRUE.equals(DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory()
                .getPeriodicalDAO()
                .delete(id, context)));
    }

    public void serveNavigate(StateNavPeriodical state) {
        Long periodicalId = state.getEntityId();
        Periodical tempPeriodical = state.getTempEntity();
        byte mask = ServiceHelper.getStateMask(periodicalId, tempPeriodical);
        state.setResultState(PERIODICAL_STATE_MAP.getOrDefault(mask,
                                                               StateNavPeriodical.State.ERROR_WRONG_PARAMETERS));
        try (DbContext dbContext = new DbContext()) {
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_PERIODICAL:
                    state.setEntity(tempPeriodical);
                    break;
                case VIEW_PERIODICAL:
                case EDIT_PERIODICAL:
                    state.setEntity(DAOFactory.getFactory().getPeriodicalDAO().findById(periodicalId, dbContext));
                    if (state.getEntity() == null) {
                        state.setResultState(StateNavPeriodical.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempEntity() == null) {
                        state.setTempEntity(state.getEntity());
                    }
                default:
            }
            PeriodicalCategoryDAO categoryDAO = DAOFactory.getFactory().getPeriodicalCategoryDAO();
            state.setCategoryNewspapers(categoryDAO.findAllByType(CategoryType.NEWSPAPER, dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(CategoryType.MAGAZINE, dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateNavPeriodical.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    public void serveSave(GenericStateSave<Periodical> state) {
        Periodical periodical = state.getEntity();
        Periodical newPeriodical;
        state.setValidationInfo(Validator.match(periodical, new PeriodicalValidating(state.getLanguage())));
        if (state.getValidationInfo().containsErrors()) {
            state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        if (periodical.notSaved()) {
            newPeriodical = DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalDAO()
                    .create(periodical, context));
        } else {
            newPeriodical = DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalDAO()
                    .update(periodical, context));
        }
        ServiceHelper.setSavedEntity(state, newPeriodical);
    }
}

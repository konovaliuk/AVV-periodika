package services;

import common.LoggerLoader;
import controller.validation.Validator;
import controller.validation.impl.CategoryValidating;
import model.CategoryType;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.PeriodicalCategoryDAO;
import persistence.dao.PeriodicalDAO;
import persistence.database.DbContext;
import services.states.GenericStateSave;
import services.states.StateNavCatalog;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogService {
    private static final Logger LOGGER = LoggerLoader.getLogger(CatalogService.class);
    private static final Byte MASK_NEW_CATEGORY = 0b0010;
    private static final Byte MASK_VIEW_CATEGORY = 0b1100;
    private static final Byte MASK_EDIT_CATEGORY = 0b1001;
    private static final Byte MASK_VIEW_ALL_CATALOG = 0b0100;
    private static final Map<Byte, StateNavCatalog.State> CATALOG_STATE_MAP;

    static {
        Map<Byte, StateNavCatalog.State> catMap = new HashMap<>();
        catMap.put(MASK_NEW_CATEGORY, StateNavCatalog.State.NEW_CATEGORY);
        catMap.put(MASK_VIEW_CATEGORY, StateNavCatalog.State.VIEW_BY_CATEGORY);
        catMap.put(MASK_EDIT_CATEGORY, StateNavCatalog.State.EDIT_CATEGORY);
        catMap.put(MASK_VIEW_ALL_CATALOG, StateNavCatalog.State.VIEW_ALL);
        CATALOG_STATE_MAP = Collections.unmodifiableMap(catMap);
    }

    CatalogService() {
    }

    public PeriodicalCategory findCategory(Long id) {
        return DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalCategoryDAO()
                .findById(id, context));
    }

    public Map<CategoryType, List<PeriodicalCategory>> findCategories() {
        Map<CategoryType, List<PeriodicalCategory>> map = new HashMap<>();
        try (DbContext dbContext = new DbContext()) {
            PeriodicalCategoryDAO dao = DAOFactory.getFactory().getPeriodicalCategoryDAO();
            map.put(CategoryType.NEWSPAPER,
                    DbContext.executeOrNull(context -> dao.findAllByType(CategoryType.NEWSPAPER, context), dbContext));
            map.put(CategoryType.MAGAZINE,
                    DbContext.executeOrNull(context -> dao.findAllByType(CategoryType.MAGAZINE, context), dbContext));
        }
        return map;
    }

    public void serveNavigate(StateNavCatalog state) {
        Long categoryId = state.getEntityId();
        PeriodicalCategory tempCategory = state.getTempEntity();
        byte mask = ServiceHelper.getStateMask(categoryId, tempCategory);
        state.setResultState(CATALOG_STATE_MAP.getOrDefault(mask, StateNavCatalog.State.ERROR_WRONG_PARAMETERS));
        try (DbContext dbContext = new DbContext()) {
            PeriodicalCategoryDAO categoryDAO = DAOFactory.getFactory().getPeriodicalCategoryDAO();
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_CATEGORY:
                    state.setEntity(tempCategory);
                    break;
                case VIEW_BY_CATEGORY:
                case EDIT_CATEGORY:
                    state.setEntity(categoryDAO.findById(categoryId, dbContext));
                    if (state.getEntity() == null) {
                        state.setResultState(StateNavCatalog.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempEntity() == null) {
                        state.setTempEntity(state.getEntity());
                    }
                case VIEW_ALL:
                    state.setPageCount(0);
                    int itemsPerPage = state.getItemsPerPage();
                    if (itemsPerPage > 0) {
                        Long itemCount = countPeriodicals(state.getEntity(), dbContext);
                        state.setPageCount((int) Math.ceil(itemCount.doubleValue() / (double) itemsPerPage));
                    }
                    state.setPeriodicals(findPeriodicals(state.getEntity(),
                                                         state.getCurrentPage(),
                                                         state.getItemsPerPage(),
                                                         dbContext));
                default:
            }
            state.setCategoryNewspapers(categoryDAO.findAllByType(CategoryType.NEWSPAPER, dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(CategoryType.MAGAZINE, dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateNavCatalog.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    public boolean serveDelete(Long id) {
        return Boolean.TRUE.equals(DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory()
                .getPeriodicalCategoryDAO()
                .delete(id, context)));
    }

    public void serveSave(GenericStateSave<PeriodicalCategory> state) {
        PeriodicalCategory category = state.getEntity();
        state.setValidationInfo(Validator.match(category, new CategoryValidating(state.getLanguage())));
        if (state.getValidationInfo().containsErrors()) {
            state.setResultState(GenericStateSave.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        PeriodicalCategory newCategory;
        if (category.notSaved()) {
            newCategory =
                    DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalCategoryDAO()
                            .create(category, context));
        } else {
            newCategory =
                    DbContext.executeTransactionOrNull(context -> DAOFactory.getFactory().getPeriodicalCategoryDAO()
                            .update(category, context));
        }
        ServiceHelper.setSavedEntity(state, newCategory);
    }

    private List<Periodical> findPeriodicals(PeriodicalCategory category,
                                             int page,
                                             int perPage,
                                             DbContext dbContext) throws DAOException {
        long limit = perPage > 0 ? perPage : Long.MAX_VALUE;
        PeriodicalDAO dao = DAOFactory.getFactory().getPeriodicalDAO();
        if (category != null && category.saved()) {
            return dao.findAllByCategoryWithLimit(category.getId(), (long) (page - 1) * perPage, limit, dbContext);
        }
        return dao.findAllWithLimit((long) (page - 1) * perPage, limit, dbContext);
    }

    private Long countPeriodicals(PeriodicalCategory category, DbContext dbContext) throws DAOException {
        PeriodicalDAO dao = DAOFactory.getFactory().getPeriodicalDAO();
        if (category != null && category.saved()) {
            return dao.countByCategory(category.getId(), dbContext);
        }
        return dao.countAll(dbContext);
    }
}

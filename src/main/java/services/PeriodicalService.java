package services;

import common.LoggerLoader;
import controller.validation.Validator;
import controller.validation.impl.CategoryValidating;
import controller.validation.impl.PeriodicalValidating;
import model.CategoryType;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.PeriodicalCategoryDAO;
import persistence.dao.PeriodicalDAO;
import persistence.database.DBContext;
import services.sto.StateHolderNavCatalog;
import services.sto.StateHolderNavPeriodical;
import services.sto.StateHolderSaveEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.ResourceManager.NULL_ID;

public class PeriodicalService {
    private static final Logger LOGGER = LoggerLoader.getLogger(PeriodicalService.class);
    private static final Byte MASK_NEW_CATEGORY = 0b0010;
    private static final Byte MASK_VIEW_CATEGORY = 0b1100;
    private static final Byte MASK_EDIT_CATEGORY = 0b1001;
    private static final Byte MASK_VIEW_ALL_CATALOG = 0b0100;
    private static final Byte MASK_NEW_PERIODICAL = 0b0010;
    private static final Byte MASK_VIEW_PERIODICAL = 0b1100;
    private static final Byte MASK_EDIT_PERIODICAL = 0b1001;
    private static final Map<Byte, StateHolderNavCatalog.State> CATALOG_STATE_MAP;
    private static final Map<Byte, StateHolderNavPeriodical.State> PERIODICAL_STATE_MAP;

    static {
        Map<Byte, StateHolderNavCatalog.State> catMap = new HashMap<>();
        catMap.put(MASK_NEW_CATEGORY, StateHolderNavCatalog.State.NEW_CATEGORY);
        catMap.put(MASK_VIEW_CATEGORY, StateHolderNavCatalog.State.VIEW_BY_CATEGORY);
        catMap.put(MASK_EDIT_CATEGORY, StateHolderNavCatalog.State.EDIT_CATEGORY);
        catMap.put(MASK_VIEW_ALL_CATALOG, StateHolderNavCatalog.State.VIEW_ALL);
        CATALOG_STATE_MAP = Collections.unmodifiableMap(catMap);
        Map<Byte, StateHolderNavPeriodical.State> pMap = new HashMap<>();
        pMap.put(MASK_NEW_PERIODICAL, StateHolderNavPeriodical.State.NEW_PERIODICAL);
        pMap.put(MASK_VIEW_PERIODICAL, StateHolderNavPeriodical.State.VIEW_PERIODICAL);
        pMap.put(MASK_EDIT_PERIODICAL, StateHolderNavPeriodical.State.EDIT_PERIODICAL);
        PERIODICAL_STATE_MAP = Collections.unmodifiableMap(pMap);
    }

    public static Map<CategoryType, List<PeriodicalCategory>> findCategories() {
        Map<CategoryType, List<PeriodicalCategory>> map = new HashMap<>();
        DBContext dbContext = new DBContext();
        PeriodicalCategoryDAO dao = DAOFactory.getPeriodicalCategoryDAO();
        map.put(CategoryType.NEWSPAPER,
                DBContext.executeOrNull(context -> dao.findAllByType(CategoryType.NEWSPAPER, context), dbContext));
        map.put(CategoryType.MAGAZINE,
                DBContext.executeOrNull(context -> dao.findAllByType(CategoryType.MAGAZINE, context), dbContext));
        dbContext.close();
        return map;
    }

    public static PeriodicalCategory findCategory(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findById(id, context));
    }

    public static Boolean serveDeleteCategory(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .delete(id, context)));
    }

    public static Boolean serveDeletePeriodical(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                .delete(id, context)));
    }

    public static Periodical findPeriodical(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                .findById(id, context));
    }

    public static void serveNavCatalog(StateHolderNavCatalog state) {
        Long categoryId = state.getCategoryId();
        PeriodicalCategory tempCategory = state.getTempCategory();
        byte mask = ServiceHelper.getStateMask(categoryId, tempCategory);
        state.setResultState(CATALOG_STATE_MAP.getOrDefault(mask, StateHolderNavCatalog.State.ERROR_WRONG_PARAMETERS));
        try (DBContext dbContext = new DBContext()) {
            PeriodicalCategoryDAO categoryDAO = DAOFactory.getPeriodicalCategoryDAO();
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_CATEGORY:
                    state.setCategory(tempCategory);
                    break;
                case VIEW_BY_CATEGORY:
                case EDIT_CATEGORY:
                    state.setCategory(categoryDAO.findById(categoryId, dbContext));
                    if (state.getCategory() == null) {
                        state.setResultState(StateHolderNavCatalog.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempCategory() == null) {
                        state.setTempCategory(state.getCategory());
                    }
                case VIEW_ALL:
                    state.setPageCount(0);
                    int itemsPerPage = state.getItemsPerPage();
                    if (itemsPerPage > 0) {
                        Long itemCount = countPeriodicals(state.getCategory(), dbContext);
                        state.setPageCount((int) Math.ceil(itemCount.doubleValue() / (double) itemsPerPage));
                    }
                    state.setPeriodicals(findPeriodicals(state.getCategory(),
                                                         state.getCurrentPage(),
                                                         state.getItemsPerPage(),
                                                         dbContext));
                default:
            }
            state.setCategoryNewspapers(categoryDAO.findAllByType(CategoryType.NEWSPAPER, dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(CategoryType.MAGAZINE, dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderNavCatalog.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    public static void serveSaveCategory(StateHolderSaveEntity<PeriodicalCategory> state) {
        PeriodicalCategory category = state.getEntity();
        state.setValidationInfo(Validator.match(category, new CategoryValidating(state.getLanguage())));
        if (state.getValidationInfo().containsErrors()) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        PeriodicalCategory newCategory;
        if (category.getId() == null) {
            newCategory = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                    .create(category, context));
        } else {
            newCategory = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                    .update(category, context));
        }
        ServiceHelper.setSavedEntity(state, newCategory);
    }

    public static void serveNavPeriodical(StateHolderNavPeriodical state) {
        Long periodicalId = state.getPeriodicalId();
        Periodical tempPeriodical = state.getTempPeriodical();
        byte mask = ServiceHelper.getStateMask(periodicalId, tempPeriodical);
        state.setResultState(PERIODICAL_STATE_MAP.getOrDefault(mask,
                                                               StateHolderNavPeriodical.State.ERROR_WRONG_PARAMETERS));
        try (DBContext dbContext = new DBContext()) {
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_PERIODICAL:
                    state.setPeriodical(tempPeriodical);
                    break;
                case VIEW_PERIODICAL:
                case EDIT_PERIODICAL:
                    state.setPeriodical(DAOFactory.getPeriodicalDAO().findById(periodicalId, dbContext));
                    if (state.getPeriodical() == null) {
                        state.setResultState(StateHolderNavPeriodical.State.ERROR_WRONG_PARAMETERS);
                        return;
                    }
                    if (state.getTempPeriodical() == null) {
                        state.setTempPeriodical(state.getPeriodical());
                    }
                default:
            }
            PeriodicalCategoryDAO categoryDAO = DAOFactory.getPeriodicalCategoryDAO();
            state.setCategoryNewspapers(categoryDAO.findAllByType(CategoryType.NEWSPAPER, dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(CategoryType.MAGAZINE, dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderNavPeriodical.State.ERROR_SERVICE_EXCEPTION);
        }
    }

    public static void serveSavePeriodical(StateHolderSaveEntity<Periodical> state) {
        Periodical periodical = state.getEntity();
        Periodical newPeriodical;
        state.setValidationInfo(Validator.match(periodical, new PeriodicalValidating(state.getLanguage())));
        if (state.getValidationInfo().containsErrors()) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        if (periodical.getId() == null) {
            newPeriodical = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                    .create(periodical, context));
        } else {
            newPeriodical = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                    .update(periodical, context));
        }
        ServiceHelper.setSavedEntity(state, newPeriodical);
    }

    private static List<Periodical> findPeriodicals(PeriodicalCategory category,
                                                    int page,
                                                    int perPage,
                                                    DBContext dbContext) throws DAOException {
        long limit = perPage > 0 ? perPage : Long.MAX_VALUE;
        PeriodicalDAO dao = DAOFactory.getPeriodicalDAO();
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return dao.findAllByCategoryWithLimit(category.getId(), (long) (page - 1) * perPage, limit, dbContext);
        }
        return dao.findAllWithLimit((long) (page - 1) * perPage, limit, dbContext);
    }

    private static Long countPeriodicals(PeriodicalCategory category, DBContext dbContext) throws DAOException {
        PeriodicalDAO dao = DAOFactory.getPeriodicalDAO();
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return dao.countByCategory(category.getId(), dbContext);
        }
        return dao.countAll(dbContext);
    }

}

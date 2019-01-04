package services;

import common.LoggerLoader;
import controller.validation.CategoryMatching;
import controller.validation.Matcher;
import controller.validation.PeriodicalMatching;
import model.Periodical;
import model.PeriodicalCategory;
import model.PeriodicalInfo;
import org.apache.log4j.Logger;
import persistence.dao.DAOException;
import persistence.dao.DAOFactory;
import persistence.dao.PeriodicalCategoryDAO;
import persistence.database.DBContext;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.ResourceManager.CATEGORY_TYPE_MAGAZINE;
import static common.ResourceManager.CATEGORY_TYPE_NEWSPAPER;
import static common.ResourceManager.RM_DAO_PERIODICAL_CATEGORY;
import static common.ViewConstants.NULL_ID;

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

    public static List<PeriodicalCategory> findNewspaperCategories() {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(CATEGORY_TYPE_NEWSPAPER), context));
    }

    public static List<PeriodicalCategory> findMagazineCategories() {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(CATEGORY_TYPE_MAGAZINE), context));
    }

    private static PeriodicalCategory findCategory(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findById(id, context));
    }

    public static Boolean serveDeleteCategory(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .delete(id, context)));
    }

    private static List<Periodical> findPeriodicals(PeriodicalCategory category,
                                                    int page,
                                                    int perPage,
                                                    DBContext dbContext) throws DAOException {
        long limit = perPage > 0 ? perPage : Long.MAX_VALUE;
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return DAOFactory.getPeriodicalDAO()
                    .findAllByCategoryWithLimit(category.getId(), (long) (page - 1) * perPage, limit, dbContext);
        }
        return DAOFactory.getPeriodicalDAO().findAllWithLimit((long) (page - 1) * perPage, limit, dbContext);
    }

    private static Long countPeriodicals(PeriodicalCategory category, DBContext dbContext) throws DAOException {
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return DAOFactory.getPeriodicalDAO().countByCategory(category.getId(), dbContext);
        }
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO().countAll(context));
    }

    public static Boolean serveDeletePeriodical(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                .delete(id, context)));
    }

    public static void serveNavCatalog(StateHolderNavCatalog state) {
        Long categoryId = state.getCategoryId();
        PeriodicalCategory tempCategory = state.getTempCategory();
        BitSet bs = new BitSet(4);
        bs.set(3, categoryId != NULL_ID);
        bs.set(2, tempCategory == null);
        bs.set(1, tempCategory != null && tempCategory.getId() == null);
        bs.set(0, tempCategory != null && categoryId.equals(tempCategory.getId()));
        byte[] bytes = bs.toByteArray();
        state.setResultState(CATALOG_STATE_MAP.getOrDefault(bytes.length > 0 ? bytes[0] : 0,
                                                            StateHolderNavCatalog.State.ERROR_WRONG_PARAMETERS));
        DBContext dbContext = new DBContext();
        PeriodicalCategoryDAO categoryDAO = DAOFactory.getPeriodicalCategoryDAO();
        try {
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
            state.setCategoryNewspapers(categoryDAO.findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(
                    CATEGORY_TYPE_NEWSPAPER), dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(
                    CATEGORY_TYPE_MAGAZINE), dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderNavCatalog.State.ERROR_SERVICE_EXCEPTION);
        } finally {
            dbContext.closeConnection();
        }
    }

    public static void serveSaveCategory(StateHolderSaveEntity<PeriodicalCategory> state) {
        PeriodicalCategory category = state.getEntity();
        PeriodicalCategory newCategory;
        state.setValidationInfo(Matcher.match(category, new CategoryMatching(state.getLanguage())));
        if (state.getValidationInfo().containsValue(false)) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_WRONG_PARAMETERS);
            return;
        }
        if (category.getId() == null) {
            newCategory = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                    .create(category, context));
        } else {
            newCategory = DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                    .update(category, context));
        }
        if (newCategory == null) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
            return;
        }
        state.setEntity(newCategory);
        state.setResultState(StateHolderSaveEntity.State.SUCCESS);
    }

    public static void serveNavPeriodical(StateHolderNavPeriodical state) {
        Long periodicalId = state.getPeriodicalId();
        Periodical tempPeriodical = state.getTempPeriodical();
        BitSet bs = new BitSet(4);
        bs.set(3, periodicalId != NULL_ID);
        bs.set(2, tempPeriodical == null);
        bs.set(1, tempPeriodical != null && tempPeriodical.getId() == null);
        bs.set(0, tempPeriodical != null && periodicalId.equals(tempPeriodical.getId()));
        byte[] bytes = bs.toByteArray();
        state.setResultState(PERIODICAL_STATE_MAP.getOrDefault(bytes.length > 0 ? bytes[0] : 0,
                                                               StateHolderNavPeriodical.State.ERROR_WRONG_PARAMETERS));
        DBContext dbContext = new DBContext();
        try {
            switch (state.getResultState()) {
                case ERROR_WRONG_PARAMETERS:
                    return;
                case NEW_PERIODICAL:
                    Long categoryId = tempPeriodical.getCategoryId();
                    PeriodicalInfo periodical = new PeriodicalInfo(tempPeriodical);
                    if (categoryId != null && categoryId != NULL_ID) {
                        PeriodicalCategory category = PeriodicalService.findCategory(categoryId);
                        if (category != null) {
                            periodical.setCategoryId(categoryId);
                            periodical.setCategoryName(category.getName());
                            periodical.setCategoryType(category.getType());
                        }
                    }
                    state.setPeriodical(periodical);
                    break;
                case VIEW_PERIODICAL:
                case EDIT_PERIODICAL:
                    state.setPeriodical(DAOFactory.getPeriodicalInfoDAO().findById(periodicalId, dbContext));
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
            state.setCategoryNewspapers(categoryDAO.findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(
                    CATEGORY_TYPE_NEWSPAPER), dbContext));
            state.setCategoryMagazines(categoryDAO.findAllByType(RM_DAO_PERIODICAL_CATEGORY.getInt(
                    CATEGORY_TYPE_MAGAZINE), dbContext));
        } catch (DAOException e) {
            LOGGER.error(e);
            state.setResultState(StateHolderNavPeriodical.State.ERROR_SERVICE_EXCEPTION);
        } finally {
            dbContext.closeConnection();
        }
    }

    public static void serveSavePeriodical(StateHolderSaveEntity<Periodical> state) {
        Periodical periodical = state.getEntity();
        Periodical newPeriodical;
        state.setValidationInfo(Matcher.match(periodical, new PeriodicalMatching(state.getLanguage())));
        if (state.getValidationInfo().containsValue(false)) {
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
        if (newPeriodical == null) {
            state.setResultState(StateHolderSaveEntity.State.ERROR_ENTITY_NOT_SAVED);
            return;
        }
        state.setEntity(newPeriodical);
        state.setResultState(StateHolderSaveEntity.State.SUCCESS);
    }
}

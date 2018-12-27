package services;

import common.LoggerLoader;
import common.ResourceManager;
import controller.commands.CommandNavCatalog;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.log4j.Logger;
import persistence.dao.DAOFactory;
import persistence.database.DBContext;

import java.util.List;

import static common.ResourceManager.CATEGORY_TYPE_MAGAZINE;
import static common.ResourceManager.CATEGORY_TYPE_NEWSPAPER;
import static common.ViewConstants.NULL_ID;

public class PeriodicalService {
    private static final Logger LOGGER = LoggerLoader.getLogger(PeriodicalService.class);
    private static final ResourceManager DAO_PERIODICAL_CAT = ResourceManager.RM_DAO_PERIODICAL_CATEGORY;

    public static List<PeriodicalCategory> findAllCategories() {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO().findAll(context));
    }

    public static List<PeriodicalCategory> findNewspaperCategories() {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findAllByType(DAO_PERIODICAL_CAT.getInt(CATEGORY_TYPE_NEWSPAPER), context));
    }

    public static List<PeriodicalCategory> findMagazineCategories() {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findAllByType(DAO_PERIODICAL_CAT.getInt(CATEGORY_TYPE_MAGAZINE), context));
    }

    public static List<Periodical> findPeriodicals(PeriodicalCategory category, int page, int perPage) {
        long limit = perPage > 0 ? perPage : Long.MAX_VALUE;
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                    .findAllByCategoryWithLimit(category.getId(), (long) (page - 1) * perPage, limit, context));
        }
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                .findAllWithLimit((long) (page - 1) * perPage, limit, context));
    }

    public static Long countPeriodicals(PeriodicalCategory category) {
        if (category != null && category.getId() != null && category.getId() != NULL_ID) {
            return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                    .countByCategory(category.getId(), context));
        }
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO().countAll(context));
    }

    public static Periodical findPeriodical(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO().findById(id, context));
    }

    public static Periodical savePeriodical(Periodical periodical, Boolean insert) {
        if (Boolean.TRUE.equals(insert)) {
            return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                    .create(periodical, context));
        }
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO().update(periodical, context));
    }

    public static Boolean deletePeriodical(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalDAO()
                .delete(id, context)));
    }

    public static PeriodicalCategory findCategory(Long id) {
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .findById(id, context));
    }

    public static PeriodicalCategory saveCategory(PeriodicalCategory category, Boolean insert) {
        if (Boolean.TRUE.equals(insert)) {
            return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                    .create(category, context));
        }
        return DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .update(category, context));
    }

    public static Boolean deleteCategory(Long id) {
        return Boolean.TRUE.equals(DBContext.executeTransactionOrNull(context -> DAOFactory.getPeriodicalCategoryDAO()
                .delete(id, context)));
    }

    public static void calcNewStateNavCatalog(CommandNavCatalog.StateHolder state) {
        long categoryId = state.getCategoryId();
        state.setResultState(CommandNavCatalog.State.ERROR_WRONG_STATE);
        if (state.isNewFlag() &&
            state.getTempCategory() != null &&
            state.getTempCategory().getId() == null) {
            state.setResultState(CommandNavCatalog.State.NEW_CATEGORY);
        }
        if (!state.isNewFlag() && categoryId == NULL_ID) {
            state.setResultState(CommandNavCatalog.State.VIEW_ALL);
        }
        if (!state.isNewFlag() && categoryId != NULL_ID && state.getTempCategory() == null) {
            state.setResultState(CommandNavCatalog.State.VIEW_BY_CATEGORY);
        }

        if (!state.isNewFlag() && categoryId != NULL_ID && state.getTempCategory() != null &&
            state.getTempCategory().getId().equals(categoryId)) {
            state.setResultState(CommandNavCatalog.State.EDIT_CATEGORY);
        }

        switch (state.getResultState()) {
            case ERROR_WRONG_STATE:
                return;
            case NEW_CATEGORY: {
                state.setCategory(state.getTempCategory());
                return;
            }
            case EDIT_CATEGORY: {
                state.setCategory(PeriodicalService.findCategory(categoryId));
                if (state.getCategory() == null) {
                    state.setResultState(CommandNavCatalog.State.ERROR_CATEGORY_NOT_FOUND);
                    return;
                }
                if (state.getTempCategory() == null) {
                    state.setTempCategory(state.getCategory());
                    state.setResultState(CommandNavCatalog.State.VIEW_BY_CATEGORY);
                }
            }
            case VIEW_BY_CATEGORY: {
                state.setCategory(PeriodicalService.findCategory(categoryId));
                if (state.getCategory() == null) {
                    state.setResultState(CommandNavCatalog.State.ERROR_CATEGORY_NOT_FOUND);
                    return;
                }
                if (state.getTempCategory() == null) {
                    state.setTempCategory(state.getCategory());
                    state.setResultState(CommandNavCatalog.State.VIEW_BY_CATEGORY);
                }
            }
        }
        state.setPageCount(getPageCount(state.getCategory(), state.getItemsPerPage()));
        state.setPeriodicals(findPeriodicals(state.getCategory(),
                                             state.getCurrentPage(),
                                             state.getItemsPerPage()));
        state.setCategoryNewspapers(findNewspaperCategories());
        state.setCategoryMagazines(findMagazineCategories());
        //            if (categoryId != NULL_ID) {
        //                if (state.isNewMode()) {
        //                    state.setCategory(state.getTempCategory());
        //                    state.setResultState(CommandNavCatalog.State.NEW_CATEGORY);
        //                } else {
        //                    state.setCategory(PeriodicalService.findCategory(categoryId));
        //                    if (state.getCategory() == null) {
        //                        state.setResultState(CommandNavCatalog.State.ERROR_CATEGORY_NOT_FOUND);
        //                        return;
        //                    }
        //                    if (state.getTempCategory() == null) {
        //                        state.setTempCategory(state.getCategory());
        //                        state.setResultState(CommandNavCatalog.State.VIEW_BY_CATEGORY);
        //                    } else {
        //                        state.setResultState(CommandNavCatalog.State.EDIT_CATEGORY);
        //                    }
        //                }
        //            }
        //            if (!state.isNewMode()) {
        //                state.setPageCount(getPageCount(state.getCategory(), state.getItemsPerPage()));
        //                state.setPeriodicals(findPeriodicals(state.getCategory(),
        //                                                     state.getCurrentPage(),
        //                                                     state.getItemsPerPage()));
        //            }
//        state.setCategoryNewspapers(findNewspaperCategories());
//        state.setCategoryMagazines(findMagazineCategories());
    }

    private static long getPageCount(PeriodicalCategory category, int itemsPerPage) {
        if (itemsPerPage > 0) {
            Long itemCount = PeriodicalService.countPeriodicals(category);
            return (int) Math.ceil(itemCount.doubleValue() / (double) itemsPerPage);
        }
        return 0;
    }

}

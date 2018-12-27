package controller.commands;

import common.LoggerLoader;
import controller.Command;
import controller.CommandResult;
import controller.SessionRequestContent;
import model.Periodical;
import model.PeriodicalCategory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import services.PeriodicalService;

import java.util.List;

import static common.ResourceManager.DEFAULT_ITEMS_PER_PAGE;
import static common.ResourceManager.PAGE_CATALOG;
import static common.ResourceManager.RM_VIEW_PAGES;
import static common.ResourceManager.URL_CATALOG;
import static common.ViewConstants.*;

public class CommandNavCatalog implements Command {
    private static final Logger LOGGER = LoggerLoader.getLogger(CommandNavCatalog.class);

    @Override
    public CommandResult execute(SessionRequestContent context) {
        StateHolder state = new StateHolder(context);
        PeriodicalService.calcNewStateNavCatalog(state);
        return writeNewState(state, context);
    }

    private CommandResult writeNewState(StateHolder state, SessionRequestContent context) {
        switch (state.getResultState()) {
            case ERROR_WRONG_STATE: {
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            }
            case ERROR_CATEGORY_NOT_FOUND: {
                return CommandResult.redirect(RM_VIEW_PAGES.get(URL_CATALOG));
            }
            case NEW_CATEGORY: {
                context.setRequestAttribute(ATTR_NAME_CATEGORY, state.getCategory());
                context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, state.getTempCategory());
            }
            case EDIT_CATEGORY: {
                context.setRequestAttribute(ATTR_NAME_CATEGORY, state.getCategory());
                context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, state.getTempCategory());
            }
            case VIEW_BY_CATEGORY: {
                context.setRequestAttribute(ATTR_NAME_CATEGORY, state.getCategory());
                context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, state.getTempCategory());
            }
            default: {
                context.setRequestAttribute(ATTR_NAME_PERIODICALS, state.getPeriodicals());
                context.setRequestAttribute(ATTR_NAME_PAGE_COUNT, state.getPageCount());
                context.setRequestAttribute(ATTR_NAME_CURRENT_PAGE, state.getCurrentPage());
                context.setSessionAttribute(ATTR_NAME_ITEMS_PER_PAGE, String.valueOf(state.getItemsPerPage()));
                context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, state.getCategoryNewspapers());
                context.setRequestAttribute(ATTR_NAME_MAGAZINES, state.getCategoryMagazines());
                return CommandResult.forward(RM_VIEW_PAGES.get(PAGE_CATALOG));
            }
        }
    }

    //    private boolean checkInputState(StateHolder stateHolder) {
    //        long categoryId = stateHolder.getCategoryId();
    //        boolean newFlag = stateHolder.newFlag();
    //        PeriodicalCategory tempCategory = stateHolder.getTempCategory();
    //        return categoryId == NULL_ID || categoryId != NEW_ID && tempCategory == null ||
    //               categoryId != NEW_ID && tempCategory.getId().equals(categoryId) ||
    //               categoryId == NEW_ID && tempCategory != null && tempCategory.getId() == null;
    //    }
    //
    //    //    private boolean setAttributes(SessionRequestContent context, long categoryId, PeriodicalCategory
    //    //    tempCategory) {
    //    //        PeriodicalCategory category = null;
    //    //        boolean category_mode = categoryId != NULL_ID;
    //    //        boolean new_category_mode = categoryId == NEW_ID;
    //    //        if (category_mode) {
    //    //            if (new_category_mode) {
    //    //                category = tempCategory;
    //    //                context.setRequestAttribute(ATTR_NAME_NEW_MODE, true);
    //    //                context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
    //    //            } else {
    //    //                category = PeriodicalService.findCategory(categoryId);
    //    //                if (category == null) {
    //    //                    context.setMessageDanger(RM_VIEW_MESSAGES.get(MESSAGE_CATEGORY_NOT_FOUND));
    //    //                    return false;
    //    //                }
    //    //                if (tempCategory == null) {
    //    //                    tempCategory = category;
    //    //                } else {
    //    //                    context.setRequestAttribute(ATTR_NAME_EDIT_MODE, true);
    //    //                }
    //    //            }
    //    //            context.setRequestAttribute(ATTR_NAME_CATEGORY, category);
    //    //            context.setRequestAttribute(ATTR_NAME_TEMP_CATEGORY, tempCategory);
    //    //        }
    //    //        if (!new_category_mode) {
    //    //            setPeriodicals(context, category);
    //    //        }
    //    //        List<PeriodicalCategory> categoryMagazines = PeriodicalService.findMagazineCategories();
    //    //        List<PeriodicalCategory> categoryNewspapers = PeriodicalService.findNewspaperCategories();
    //    //        context.setRequestAttribute(ATTR_NAME_NEWSPAPERS, categoryNewspapers);
    //    //        context.setRequestAttribute(ATTR_NAME_MAGAZINES, categoryMagazines);
    //    //        return true;
    //    //    }
    //
    //    //    private void setPeriodicals(SessionRequestContent context, PeriodicalCategory category) {
    //    //        int currentPage = NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_PAGE), 1);
    //    //        int itemsPerPage = getItemsPerPage(context);
    //    //        long pageCount = getPageCount(category, itemsPerPage);
    //    //        List<Periodical> periodicals = PeriodicalService.findPeriodicals(category, currentPage,
    //    itemsPerPage);
    //    //        context.setRequestAttribute(ATTR_NAME_PERIODICALS, periodicals);
    //    //        context.setRequestAttribute(ATTR_NAME_PAGE_COUNT, pageCount);
    //    //        context.setRequestAttribute(ATTR_NAME_CURRENT_PAGE, currentPage);
    //    //        context.setSessionAttribute(ATTR_NAME_ITEMS_PER_PAGE, String.valueOf(itemsPerPage));
    //    //    }
    //
    //    //    private int getItemsPerPage(SessionRequestContent context) {
    //    //        return NumberUtils.toInt(StringUtils.defaultIfEmpty(context.getRequestParameter
    //    (ATTR_NAME_ITEMS_PER_PAGE),
    //    //                                                            (String) context.getSessionAttribute(
    //    //                                                                    ATTR_NAME_ITEMS_PER_PAGE)),
    //    //                                 RM_VIEW_PAGES.getInt(DEFAULT_ITEMS_PER_PAGE));
    //    //    }
    //
    //    //    private long getPageCount(PeriodicalCategory category, int itemsPerPage) {
    //    //        if (itemsPerPage > 0) {
    //    //            Long itemCount = PeriodicalService.countPeriodicals(category);
    //    //            return (int) Math.ceil(itemCount.doubleValue() / (double) itemsPerPage);
    //    //        }
    //    //        return 0;
    //    //    }

    public enum State {
        ERROR_WRONG_STATE,
        NEW_CATEGORY,
        ERROR_CATEGORY_NOT_FOUND,
        EDIT_CATEGORY,
        VIEW_BY_CATEGORY,
        VIEW_ALL;
    }

    public final static class StateHolder {
        private final SessionRequestContent context;
        private final long categoryId;
        private final boolean newFlag;
        private State resultState;
        private PeriodicalCategory tempCategory;
        private String message;
        private long pageCount;
        private int currentPage;
        private int itemsPerPage;
        private boolean newMode;
        private boolean editMode;
        private List<PeriodicalCategory> categoryMagazines;
        private List<PeriodicalCategory> categoryNewspapers;
        private List<Periodical> periodicals;
        private PeriodicalCategory category;

        StateHolder(SessionRequestContent context) {
            this.context = context;
            categoryId = NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_CATEGORY_ID), NULL_ID);
            newFlag = 1 == NumberUtils.toLong(context.getRequestParameter(PARAM_NAME_NEW_MODE), 0);
            tempCategory = (PeriodicalCategory) context.getSessionAttribute(ATTR_NAME_TEMP_CATEGORY);
            context.removeSessionAttribute(ATTR_NAME_TEMP_CATEGORY);
            itemsPerPage =
                    NumberUtils.toInt(StringUtils.defaultIfEmpty(context.getRequestParameter(ATTR_NAME_ITEMS_PER_PAGE),
                                                                 (String) context.getSessionAttribute(
                                                                         ATTR_NAME_ITEMS_PER_PAGE)),
                                      RM_VIEW_PAGES.getInt(DEFAULT_ITEMS_PER_PAGE));
            currentPage = NumberUtils.toInt(context.getRequestParameter(PARAM_NAME_PAGE), 1);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getPageCount() {
            return pageCount;
        }

        public void setPageCount(long pageCount) {
            this.pageCount = pageCount;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getItemsPerPage() {
            return itemsPerPage;
        }

        public boolean isNewMode() {
            return newMode;
        }

        public void setNewMode(boolean newMode) {
            this.newMode = newMode;
        }

        public boolean isEditMode() {
            return editMode;
        }

        public void setEditMode(boolean editMode) {
            this.editMode = editMode;
        }

        public List<PeriodicalCategory> getCategoryMagazines() {
            return categoryMagazines;
        }

        public void setCategoryMagazines(List<PeriodicalCategory> categoryMagazines) {
            this.categoryMagazines = categoryMagazines;
        }

        public List<PeriodicalCategory> getCategoryNewspapers() {
            return categoryNewspapers;
        }

        public void setCategoryNewspapers(List<PeriodicalCategory> categoryNewspapers) {
            this.categoryNewspapers = categoryNewspapers;
        }

        public List<Periodical> getPeriodicals() {
            return periodicals;
        }

        public void setPeriodicals(List<Periodical> periodicals) {
            this.periodicals = periodicals;
        }

        public PeriodicalCategory getCategory() {
            return category;
        }

        public void setCategory(PeriodicalCategory category) {
            this.category = category;
        }

        public State getResultState() {
            return resultState;
        }

        public void setResultState(State resultState) {
            this.resultState = resultState;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public boolean isNewFlag() {
            return newFlag;
        }

        public PeriodicalCategory getTempCategory() {
            return tempCategory;
        }

        public void setTempCategory(PeriodicalCategory tempCategory) {
            this.tempCategory = tempCategory;
        }

    }
}

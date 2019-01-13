package services.sto;

import model.Periodical;
import model.PeriodicalCategory;

import java.util.List;

public final class StateHolderNavCatalog {
    private State resultState;
    private long categoryId;
    private PeriodicalCategory category;
    private PeriodicalCategory tempCategory;
    private List<PeriodicalCategory> categoryMagazines;
    private List<PeriodicalCategory> categoryNewspapers;
    private long pageCount;
    private int currentPage;
    private int itemsPerPage;
    private List<Periodical> periodicals;

    public StateHolderNavCatalog() { }

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
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

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public PeriodicalCategory getTempCategory() {
        return tempCategory;
    }

    public void setTempCategory(PeriodicalCategory tempCategory) {
        this.tempCategory = tempCategory;
    }

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_CATEGORY,
        EDIT_CATEGORY,
        VIEW_BY_CATEGORY,
        VIEW_ALL
    }
}

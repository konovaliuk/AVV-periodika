package services.states;

import model.Periodical;
import model.PeriodicalCategory;

import java.util.List;

public class StateNavCatalog extends GenericStateNavigate<PeriodicalCategory, StateNavCatalog.State> {
    private List<PeriodicalCategory> categoryMagazines;
    private List<PeriodicalCategory> categoryNewspapers;
    private List<Periodical> periodicals;
    private long pageCount;
    private int currentPage;
    private int itemsPerPage;

    public StateNavCatalog() { }

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

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_CATEGORY,
        EDIT_CATEGORY,
        VIEW_BY_CATEGORY,
        VIEW_ALL
    }
}

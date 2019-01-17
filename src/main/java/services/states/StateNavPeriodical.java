package services.states;

import model.Periodical;
import model.PeriodicalCategory;

import java.util.List;

public class StateNavPeriodical extends GenericStateNavigate<Periodical, StateNavPeriodical.State> {
    private List<PeriodicalCategory> categoryMagazines;
    private List<PeriodicalCategory> categoryNewspapers;

    public StateNavPeriodical() { }

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

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_PERIODICAL,
        EDIT_PERIODICAL,
        VIEW_PERIODICAL
    }
}

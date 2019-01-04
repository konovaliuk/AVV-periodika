package services;

import model.Periodical;
import model.PeriodicalCategory;
import model.PeriodicalInfo;

import java.util.List;

public class StateHolderNavPeriodical {
    private State resultState;
    private long periodicalId;
    private Periodical tempPeriodical;
    private PeriodicalInfo periodical;
    private List<PeriodicalCategory> categoryMagazines;
    private List<PeriodicalCategory> categoryNewspapers;

    public StateHolderNavPeriodical() { }

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
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

    public Periodical getTempPeriodical() {
        return tempPeriodical;
    }

    public void setTempPeriodical(Periodical tempPeriodical) {
        this.tempPeriodical = tempPeriodical;
    }

    public long getPeriodicalId() {
        return periodicalId;
    }

    public void setPeriodicalId(long periodicalId) {
        this.periodicalId = periodicalId;
    }

    public PeriodicalInfo getPeriodical() {
        return periodical;
    }

    public void setPeriodical(PeriodicalInfo periodical) {
        this.periodical = periodical;
    }

    public enum State {
        ERROR_SERVICE_EXCEPTION,
        ERROR_WRONG_PARAMETERS,
        NEW_PERIODICAL,
        EDIT_PERIODICAL,
        VIEW_PERIODICAL
    }
}

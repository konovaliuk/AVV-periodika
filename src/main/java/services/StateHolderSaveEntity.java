package services;

import java.util.Map;

public class StateHolderSaveEntity<T extends model.Entity> {
    private State resultState;
    private String language;
    private T entity;
    private Map<String, Boolean> validationInfo;

    public StateHolderSaveEntity() { }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Map<String, Boolean> getValidationInfo() {
        return validationInfo;
    }

    public void setValidationInfo(Map<String, Boolean> validationInfo) {
        this.validationInfo = validationInfo;
    }

    public enum State {
        ERROR_WRONG_PARAMETERS,
        ERROR_ENTITY_NOT_SAVED,
        SUCCESS
    }
}

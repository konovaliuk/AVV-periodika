package services.sto;

import controller.validation.ValidationData;
import model.User;

public class StateHolderSaveEntity<T extends model.Entity> {
    private State resultState;
    private String language;
    private User user;
    private T entity;
    private ValidationData validationInfo;

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

    public ValidationData getValidationInfo() {
        return validationInfo;
    }

    public void setValidationInfo(ValidationData validationInfo) {
        this.validationInfo = validationInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum State {
        ERROR_WRONG_PARAMETERS,
        ERROR_ENTITY_NOT_SAVED,
        SUCCESS
    }
}

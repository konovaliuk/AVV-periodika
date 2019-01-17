package services.states;

import model.User;

public class GenericStateNavigate<T, S> {
    private S resultState;
    private long entityId;
    private User user;
    private T tempEntity;
    private T entity;

    public GenericStateNavigate() { }

    public S getResultState() {
        return resultState;
    }

    public void setResultState(S resultState) {
        this.resultState = resultState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public T getTempEntity() {
        return tempEntity;
    }

    public void setTempEntity(T tempEntity) {
        this.tempEntity = tempEntity;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

}

package model;

import java.io.Serializable;

public interface Entity<PK extends Serializable> {
    long NULL_ID = 0;

    PK getId();

    void setId(PK id);

    default boolean notSaved() {
        return !saved();
    }

    default boolean saved() {
        return getId() != null && !getId().equals(NULL_ID);
    }
}

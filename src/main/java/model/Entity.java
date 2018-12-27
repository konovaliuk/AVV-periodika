package model;

import java.io.Serializable;

public interface Entity<PK extends Serializable> {

    PK getId();

    void setId(PK id);
}

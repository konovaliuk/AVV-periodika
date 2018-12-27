package model;

import java.util.Objects;

public class UserType implements Entity<Long> {
    private Long id;
    private String name;

    public UserType() {
    }

    public UserType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserType)) {
            return false;
        }
        UserType userType = (UserType) o;
        return Objects.equals(id, userType.id) && Objects.equals(name, userType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

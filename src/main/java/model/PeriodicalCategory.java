package model;

import java.util.Objects;

public class PeriodicalCategory implements Entity<Long> {
    private Long id;
    private String name;
    private CategoryType type;
    private String description;

    public PeriodicalCategory() {
    }

    public PeriodicalCategory(Long id, String name, CategoryType type, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
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

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PeriodicalCategory category = (PeriodicalCategory) o;
        return Objects.equals(id, category.id) &&
               Objects.equals(name, category.name) &&
               type == category.type &&
               Objects.equals(description, category.description);
    }
}

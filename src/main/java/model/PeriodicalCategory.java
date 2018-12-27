package model;

public class PeriodicalCategory implements Entity<Long> {
    private Long id;
    private String name;
    private Integer type;
    private String description;

    public PeriodicalCategory() {
    }

    public PeriodicalCategory(Long id, String name, Integer type, String description) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

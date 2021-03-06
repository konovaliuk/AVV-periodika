package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Periodical implements Entity<Long> {
    private Long id;
    private Long categoryId;
    private String title;
    private String description;
    private Integer minSubscriptionPeriod;
    private Integer issuesPerPeriod;
    private BigDecimal pricePerPeriod;
    private String categoryName;
    private CategoryType categoryType;

    public Periodical() {
    }

    public Periodical(Long id,
                      Long categoryId,
                      String title,
                      String description,
                      Integer minSubscriptionPeriod,
                      Integer issuesPerPeriod,
                      BigDecimal pricePerPeriod) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.minSubscriptionPeriod = minSubscriptionPeriod;
        this.issuesPerPeriod = issuesPerPeriod;
        this.pricePerPeriod = pricePerPeriod;
    }

    public Periodical(Long id,
                      Long categoryId,
                      String title,
                      String description,
                      Integer minSubscriptionPeriod,
                      Integer issuesPerPeriod,
                      BigDecimal pricePerPeriod,
                      String categoryName,
                      CategoryType categoryType) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.minSubscriptionPeriod = minSubscriptionPeriod;
        this.issuesPerPeriod = issuesPerPeriod;
        this.pricePerPeriod = pricePerPeriod;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinSubscriptionPeriod() {
        return minSubscriptionPeriod;
    }

    public void setMinSubscriptionPeriod(Integer minSubscriptionPeriod) {
        this.minSubscriptionPeriod = minSubscriptionPeriod;
    }

    public Integer getIssuesPerPeriod() {
        return issuesPerPeriod;
    }

    public void setIssuesPerPeriod(Integer issuesPerPeriod) {
        this.issuesPerPeriod = issuesPerPeriod;
    }

    public BigDecimal getPricePerPeriod() {
        return pricePerPeriod;
    }

    public void setPricePerPeriod(BigDecimal pricePerPeriod) {
        this.pricePerPeriod = pricePerPeriod;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                            categoryId,
                            title,
                            description,
                            minSubscriptionPeriod,
                            issuesPerPeriod,
                            pricePerPeriod,
                            categoryName,
                            categoryType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Periodical that = (Periodical) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(categoryId, that.categoryId) &&
               Objects.equals(title, that.title) &&
               Objects.equals(description, that.description) &&
               Objects.equals(minSubscriptionPeriod, that.minSubscriptionPeriod) &&
               Objects.equals(issuesPerPeriod, that.issuesPerPeriod) &&
               Objects.equals(pricePerPeriod, that.pricePerPeriod) &&
               Objects.equals(categoryName, that.categoryName) &&
               categoryType == that.categoryType;
    }
}

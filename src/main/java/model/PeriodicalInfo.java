package model;

import java.math.BigDecimal;

public class PeriodicalInfo extends Periodical implements Entity<Long> {
    private String categoryName;
    private Integer categoryType;

    public PeriodicalInfo() { }

    public PeriodicalInfo(Long id,
                          Long categoryId,
                          String title,
                          String description,
                          Integer minSubscriptionPeriod,
                          Integer issuesPerPeriod,
                          BigDecimal pricePerPeriod,
                          String subscriptionIndex,
                          String categoryName,
                          Integer categoryType) {
        super(id,
              categoryId,
              title,
              description,
              minSubscriptionPeriod,
              issuesPerPeriod,
              pricePerPeriod,
              subscriptionIndex);
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public PeriodicalInfo(Periodical periodical) {
        super(periodical.getId(),
              periodical.getCategoryId(),
              periodical.getTitle(),
              periodical.getDescription(),
              periodical.getMinSubscriptionPeriod(),
              periodical.getIssuesPerPeriod(),
              periodical.getPricePerPeriod(),
              periodical.getSubscriptionIndex());
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }
}

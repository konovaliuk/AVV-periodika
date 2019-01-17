package model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

public class Subscription implements Entity<Long> {
    private Long id;
    private Long userId;
    private Long periodicalId;
    private Long paymentId;
    private YearMonth periodStart;
    private Integer periodCount;
    private YearMonth periodEnd;
    private Integer quantity;
    private BigDecimal sum;

    public Subscription() {
    }

    public Subscription(Long id,
                        Long userId,
                        Long periodicalId,
                        Long paymentId,
                        YearMonth periodStart,
                        Integer periodCount,
                        YearMonth periodEnd,
                        Integer quantity,
                        BigDecimal sum) {
        this.id = id;
        this.userId = userId;
        this.periodicalId = periodicalId;
        this.paymentId = paymentId;
        this.periodStart = periodStart;
        this.periodCount = periodCount;
        this.periodEnd = periodEnd;
        this.quantity = quantity;
        this.sum = sum;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeriodicalId() {
        return periodicalId;
    }

    public void setPeriodicalId(Long periodicalId) {
        this.periodicalId = periodicalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public YearMonth getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(YearMonth periodStart) {
        this.periodStart = periodStart;
    }

    public YearMonth getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(YearMonth periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Integer getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(Integer periodCount) {
        this.periodCount = periodCount;
    }

    public YearMonth getMinPeriodStart() {
        return YearMonth.now().plusMonths(1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, periodicalId, paymentId, periodStart, periodCount, periodEnd, quantity, sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(periodicalId, that.periodicalId) &&
               Objects.equals(paymentId, that.paymentId) &&
               Objects.equals(periodStart, that.periodStart) &&
               Objects.equals(periodCount, that.periodCount) &&
               Objects.equals(periodEnd, that.periodEnd) &&
               Objects.equals(quantity, that.quantity) &&
               Objects.equals(sum, that.sum);
    }
}

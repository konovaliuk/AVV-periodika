package model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Payment implements Entity<Long> {
    private Long id;
    private Instant date;
    private BigDecimal sum;
    private List<SubscriptionInfo> subscriptions;

    public Payment() {
    }

    public Payment(Long id, Instant date, BigDecimal sum) {
        this.id = id;
        this.date = date;
        this.sum = sum;
    }

    public Payment(Instant date, BigDecimal sum) {
        this.date = date;
        this.sum = sum;
    }

    public Payment(Instant date, BigDecimal sum, List<SubscriptionInfo> subscriptions) {
        this(date, sum);
        this.subscriptions = subscriptions;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public List<SubscriptionInfo> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionInfo> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, sum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
               Objects.equals(date, payment.date) &&
               Objects.equals(sum, payment.sum);
    }
}

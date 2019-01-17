package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.Objects;

public class SubscriptionInfo extends Subscription {
    private Periodical periodical;

    public SubscriptionInfo() {
    }

    public SubscriptionInfo(Long id,
                            Long userId,
                            Long periodicalId,
                            Long paymentId,
                            YearMonth periodStart,
                            Integer periodCount,
                            YearMonth periodEnd,
                            Integer quantity,
                            BigDecimal sum,
                            Periodical periodical) {
        super(id, userId, periodicalId, paymentId, periodStart, periodCount, periodEnd, quantity, sum);
        this.periodical = periodical;
    }

    public SubscriptionInfo(Subscription subscription, Periodical periodical) {
        this(subscription.getId(),
             subscription.getUserId(),
             subscription.getPeriodicalId(),
             subscription.getPaymentId(),
             subscription.getPeriodStart(),
             subscription.getPeriodCount(),
             subscription.getPeriodEnd(),
             subscription.getQuantity(),
             subscription.getSum(),
             periodical);
    }

    public Periodical getPeriodical() {
        return periodical;
    }

    public void setPeriodical(Periodical periodical) {
        this.periodical = periodical;
    }

    public BigDecimal getCalcSum() {
        return periodical.getPricePerPeriod()
                .multiply(BigDecimal.valueOf(getPeriodCount()))
                .multiply(BigDecimal.valueOf(getQuantity()));
    }

    public Integer getCalcDuration() {
        return periodical.getMinSubscriptionPeriod() * getPeriodCount();
    }

    public Integer getDuration() {
        if (getPeriodStart() == null || getPeriodEnd() == null) {
            return 0;
        }
        return Period.between(LocalDate.from(getPeriodStart().atDay(1)),
                              LocalDate.from(getPeriodEnd().atEndOfMonth())).getMonths() + 1;
    }

    public YearMonth getCalcPeriodEnd() {
        return getPeriodStart().plusMonths(getCalcDuration() - 1);
    }

    public void fillCalculatedFields() {
        if (periodical != null) {
            setPeriodEnd(getCalcPeriodEnd());
            setSum(getCalcSum());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), periodical);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SubscriptionInfo that = (SubscriptionInfo) o;
        return Objects.equals(periodical, that.periodical);
    }
}

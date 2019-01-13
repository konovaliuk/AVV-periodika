package model;

import java.math.BigDecimal;
import java.time.YearMonth;

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

    public YearMonth getCalcPeriodEnd() {
        return getPeriodStart().plusMonths(getCalcDuration() - 1);
    }

    public void fillCalculatedFields() {
        if (periodical != null) {
            setPeriodEnd(getCalcPeriodEnd());
            setSum(getCalcSum());
        }
    }
}

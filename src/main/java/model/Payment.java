package model;

import java.math.BigDecimal;
import java.time.Instant;

public class Payment implements Entity<Long> {
    private Long id;
    private Instant date;
    private BigDecimal sum;

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
}

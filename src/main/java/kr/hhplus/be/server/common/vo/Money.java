package kr.hhplus.be.server.common.vo;

import jakarta.persistence.Column;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Money {

    @Column(nullable = false)
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    public static Money wons(BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public Money multiply(int multiplier) {
        return new Money(this.value.multiply(BigDecimal.valueOf(multiplier)));
    }

    public Money multiply(long multiplier) {
        return new Money(this.value.multiply(BigDecimal.valueOf(multiplier)));
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.value.compareTo(other.value) >= 0;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

package kr.hhplus.be.server.common.vo;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Money {
    private final BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }
    public static Money wons(BigDecimal value) {
        return new Money(value);
    }
    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
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
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "₩" + value;
    }
}

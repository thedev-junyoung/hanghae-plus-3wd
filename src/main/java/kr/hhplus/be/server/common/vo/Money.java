package kr.hhplus.be.server.common.vo;

import java.util.Objects;

public class Money {

    private final long value;

    private Money(long value) {
        this.value = value;
    }

    public static Money wons(long amount) {
        return new Money(amount);
    }

    public Money add(Money other) {
        return new Money(this.value + other.value);
    }

    public Money subtract(Money other) {
        return new Money(this.value - other.value);
    }

    public Money multiply(int multiplier) {
        return new Money(this.value * multiplier);
    }

    public Money multiply(long multiplier) {
        return new Money(this.value * multiplier);
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return this.value >= other.value;
    }

    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "원";
    }
}

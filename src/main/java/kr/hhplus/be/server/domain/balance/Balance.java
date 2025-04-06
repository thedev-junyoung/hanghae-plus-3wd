package kr.hhplus.be.server.domain.balance;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.balance.exception.NotEnoughBalanceException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Balance {
    @EqualsAndHashCode.Include
    private final Long id;
    private final Long userId;
    private Money amount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Balance createNew(Long id, Long userId, Money amount) {
        LocalDateTime now = LocalDateTime.now();
        return new Balance(id, userId, amount, now, now);
    }


    public void charge(Money value) {
        this.amount = this.amount.add(value);
        this.updatedAt = LocalDateTime.now();
    }

    public void decrease(Money value) {
        if (!amount.isGreaterThanOrEqual(value)) {
            throw new NotEnoughBalanceException();
        }
        amount = amount.subtract(value);
        updatedAt = LocalDateTime.now();
    }


    public boolean hasEnough(Money value) {
        return this.amount.isGreaterThanOrEqual(value);
    }
}

package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.balance.exception.NotEnoughBalanceException;
import kr.hhplus.be.server.common.vo.Money;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Embedded
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Balance(Long id, Long userId, Money amount) {
        LocalDateTime now = LocalDateTime.now();
        this.id = id;
        this.userId = userId;
        this.amount = amount.getValue();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public static Balance createNew(Long id, Long userId, Money amount) {
        return new Balance(id, userId, amount);
    }

    public void charge(Money value) {
        Money current = Money.wons(amount);
        Money charged = current.add(value);
        this.amount = charged.value();
        this.updatedAt = LocalDateTime.now();
    }

    public void decrease(Money value) {
        Money current = Money.wons(amount);
        if (!current.isGreaterThanOrEqual(value)) {
            throw new NotEnoughBalanceException();
        }
        this.amount = current.subtract(value).value();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasEnough(Money value) {
        return Money.wons(amount).isGreaterThanOrEqual(value);
    }
}

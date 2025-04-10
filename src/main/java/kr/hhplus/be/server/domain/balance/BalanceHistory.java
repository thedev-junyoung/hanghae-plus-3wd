package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BalanceChangeType type;

    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public BalanceHistory(Long userId, BigDecimal amount, BalanceChangeType type, String reason, LocalDateTime createdAt) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public static BalanceHistory of(Long userId, BigDecimal amount, BalanceChangeType type, String reason) {
        return new BalanceHistory(userId, amount, type, reason, LocalDateTime.now());
    }

    public boolean isChargeHistory() {
        return this.type == BalanceChangeType.CHARGE;
    }

    public boolean isDeductHistory() {
        return this.type == BalanceChangeType.DEDUCT;
    }
}

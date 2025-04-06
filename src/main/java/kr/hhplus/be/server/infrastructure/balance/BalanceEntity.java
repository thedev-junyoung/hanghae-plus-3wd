package kr.hhplus.be.server.infrastructure.balance;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.balance.Balance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "balances")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceEntity {

    @Id
    private Long id;

    private Long userId;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // --- Domain -> Entity ---/**/
    public static BalanceEntity from(Balance domain) {
        return new BalanceEntity(
                domain.getId(),
                domain.getUserId(),
                domain.getAmount().value(), // Money → BigDecimal
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    // --- Entity -> Domain ---
    public Balance toDomain() {
        return new Balance(
                id,
                userId,
                Money.wons(amount), // BigDecimal → Money
                createdAt,
                updatedAt
        );
    }

}
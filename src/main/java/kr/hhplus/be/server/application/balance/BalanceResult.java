package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.Balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceResult(
        Long id,
        Long userId,
        BigDecimal balance,
        LocalDateTime updatedAt
) {
    public static BalanceResult from(Balance balance) {
        return new BalanceResult(
                balance.getId(),
                balance.getUserId(),
                balance.getAmount().value(),
                balance.getUpdatedAt()
        );
    }
}

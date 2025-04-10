package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.Balance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BalanceResult(
        Long userId,
        Long balance,
        LocalDateTime updatedAt
) {
    public static BalanceResult fromInfo(BalanceInfo info) {
        return new BalanceResult(
                info.userId(),
                info.amount(),
                info.updatedAt()
        );
    }

    public static BalanceResult fromDomain(Balance balance) {
        return new BalanceResult(
                balance.getUserId(),
                balance.getAmount(), // Money 내부 값 꺼냄
                balance.getUpdatedAt()
        );
    }
}

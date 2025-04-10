package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceChangeType;

import java.math.BigDecimal;

public record RecordBalanceHistoryCommand(
        Long userId,
        long amount,
        BalanceChangeType type,
        String reason
) {}


package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceChangeType;


public record RecordBalanceHistoryCommand(
        Long userId,
        long amount,
        BalanceChangeType type,
        String reason
) {}


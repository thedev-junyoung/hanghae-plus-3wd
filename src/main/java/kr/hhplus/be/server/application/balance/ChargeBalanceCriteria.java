package kr.hhplus.be.server.application.balance;

import java.math.BigDecimal;

public record ChargeBalanceCriteria(
        Long userId,
        long amount,
        String reason
) {}

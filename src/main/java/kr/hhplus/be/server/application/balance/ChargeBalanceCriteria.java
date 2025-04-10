package kr.hhplus.be.server.application.balance;

import java.math.BigDecimal;

public record ChargeBalanceCriteria(
        Long userId,
        BigDecimal amount,
        String reason
) {}

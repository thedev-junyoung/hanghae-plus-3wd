package kr.hhplus.be.server.application.balance;

import java.math.BigDecimal;

public record ChargeBalanceCommand(
        Long userId,
        BigDecimal amount
) {}

package kr.hhplus.be.server.application.balance;


import java.math.BigDecimal;


public record DecreaseBalanceCommand(
        Long userId,
        BigDecimal amount
) {}

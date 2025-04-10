package kr.hhplus.be.server.application.balance;


public record ChargeBalanceCommand(
        Long userId,
        long amount
) {}

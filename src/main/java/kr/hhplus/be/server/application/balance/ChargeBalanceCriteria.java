package kr.hhplus.be.server.application.balance;


public record ChargeBalanceCriteria(
        Long userId,
        long amount,
        String reason
) {}

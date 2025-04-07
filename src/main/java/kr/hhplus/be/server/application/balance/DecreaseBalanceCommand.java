package kr.hhplus.be.server.application.balance;


import java.math.BigDecimal;


public record DecreaseBalanceCommand(
        Long userId,
        BigDecimal amount
) {
    public DecreaseBalanceCommand {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("감액 금액은 0보다 커야 합니다.");
        }
    }

}

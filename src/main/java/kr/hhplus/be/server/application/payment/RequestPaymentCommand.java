package kr.hhplus.be.server.application.payment;


import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;


public record RequestPaymentCommand(
        String orderId,
        Long userId,
        String method  // 예: "BALANCE", "CARD"
) {
    public DecreaseBalanceCommand toDecreaseBalanceCommand(long amount) {
        return new DecreaseBalanceCommand(userId, amount);
    }
}

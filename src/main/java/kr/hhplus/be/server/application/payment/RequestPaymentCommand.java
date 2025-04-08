package kr.hhplus.be.server.application.payment;


import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.domain.common.vo.Money;

public record RequestPaymentCommand(
        String orderId,
        Long userId,
        String method  // 예: "BALANCE", "CARD"
) {
    public DecreaseBalanceCommand toDecreaseBalanceCommand(Money amount) {
        return new DecreaseBalanceCommand(userId, amount.value());
    }
}

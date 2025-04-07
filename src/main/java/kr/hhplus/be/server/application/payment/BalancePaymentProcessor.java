package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalancePaymentProcessor implements PaymentProcessor {

    private final BalanceService balanceService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Override
    public void process(RequestPaymentCommand command, Order order, Payment payment) {
        DecreaseBalanceCommand decreaseCommand = command.toDecreaseBalanceCommand(order.getTotalAmount());
        boolean deducted = balanceService.decreaseBalance(decreaseCommand);

        if (!deducted) {
            paymentService.markFailure(payment);
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        paymentService.markSuccess(payment);
        orderService.markConfirmed(order);
    }

}

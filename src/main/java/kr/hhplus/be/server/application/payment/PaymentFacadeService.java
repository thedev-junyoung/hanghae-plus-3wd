package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.infrastructure.payment.ExternalPaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService implements PaymentUseCase {

    private final PaymentService paymentService; // 내부 로직 담당 서비스
    private final OrderService orderService;
    private final BalanceService balanceService;
    private final ExternalPaymentGateway externalGateway;

    @Override
    public PaymentResult requestPayment(RequestPaymentCommand command) {
        Order order = orderService.getOrderForPayment(command.orderId());
        Money amount = order.getTotalAmount();

        Payment payment = paymentService.initiate(command.orderId(), amount, command.method());

        boolean success = paymentService.process(command, order, payment);
        if (!success) {
            paymentService.markFailure(payment);
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        paymentService.markSuccess(payment);
        orderService.markConfirmed(order);

        return PaymentResult.from(payment);
    }

    @Override
    public PaymentResult confirmPayment(ConfirmPaymentCommand command) {
        // 1. 결제 엔티티 조회
        Payment payment = paymentService.getByPgTraansactionId(command.pgTransactionId());

        // 2. 이미 완료된 경우 예외 방지
        if (payment.isCompleted()) {
            return PaymentResult.from(payment);
        }

        // 3. 결제 성공 처리
        paymentService.markSuccess(payment);

        // 4. 주문 상태도 CONFIRMED 로 변경
        Order order = orderService.getOrderForPayment(payment.getOrderId());
        orderService.markConfirmed(order);

        return PaymentResult.from(payment);
    }



}


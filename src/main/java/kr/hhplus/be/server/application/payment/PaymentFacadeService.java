package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.application.orderexport.OrderExportCommand;
import kr.hhplus.be.server.application.orderexport.OrderExportService;
import kr.hhplus.be.server.application.productstatistics.ProductStatisticsService;
import kr.hhplus.be.server.application.productstatistics.RecordSalesCommand;
import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.orderexport.OrderExportPayload;
import kr.hhplus.be.server.domain.payment.ExternalPaymentGateway;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.exception.ExternalSystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacadeService implements PaymentUseCase {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final BalanceService balanceService;
    private final ExternalPaymentGateway externalGateway;
    private final ProductStatisticsService productStatisticsService;
    private final OrderExportService orderExportService;

    @Override
    public PaymentResult requestPayment(RequestPaymentCommand command) {
        Order order = orderService.getOrderForPayment(command.orderId());
        Money amount = order.getTotalAmount();

        Payment payment = paymentService.initiate(command.orderId(), amount, command.method());

        // 실제 외부 결제 요청
        boolean paymentRequested = externalGateway.requestPayment(command.orderId());
        if (!paymentRequested) {
            paymentService.markFailure(payment);
            throw new ExternalSystemException("외부 결제 요청 실패");
        }

        // 잔액 차감 등 내부 처리
        paymentService.process(command, order, payment);

        // 통계 처리
        order.getItems().forEach(item ->
                productStatisticsService.record(
                        new RecordSalesCommand(
                                item.getProductId(),
                                item.getQuantity(),
                                item.calculateTotal().value().longValue()
                        )
                )
        );

        // 외부 주문 전송
        orderExportService.export(
                new OrderExportCommand(OrderExportPayload.from(order))
        );

        return PaymentResult.from(payment);
    }

    @Override
    public PaymentResult confirmPayment(ConfirmPaymentCommand command) {
        Payment payment = paymentService.getByPgTraansactionId(command.pgTransactionId());

        if (payment.isCompleted()) {
            return PaymentResult.from(payment);
        }

        // 외부 결제 시스템 확인
        boolean confirmed = externalGateway.confirmPayment(command.pgTransactionId());
        if (!confirmed) {
            paymentService.markFailure(payment);
            throw new ExternalSystemException("외부 결제 확인 실패");
        }

        paymentService.markSuccess(payment);

        Order order = orderService.getOrderForPayment(payment.getOrderId());
        orderService.markConfirmed(order);

        return PaymentResult.from(payment);
    }
}

package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.infrastructure.payment.ExternalPaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentFacadeServiceTest {

    private PaymentService paymentService;
    private OrderService orderService;
    private BalanceService balanceService;
    private ExternalPaymentGateway externalGateway;

    private PaymentFacadeService paymentFacadeService;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        orderService = mock(OrderService.class);
        balanceService = mock(BalanceService.class);
        externalGateway = mock(ExternalPaymentGateway.class);

        paymentFacadeService = new PaymentFacadeService(paymentService, orderService, balanceService, externalGateway);
    }

    @Test
    @DisplayName("결제 요청 성공")
    void requestPayment_success() {
        // Given
        RequestPaymentCommand command = new RequestPaymentCommand("order-123", 1L, "BALANCE");

        Order mockOrder = mock(Order.class);
        when(orderService.getOrderForPayment("order-123")).thenReturn(mockOrder);
        when(mockOrder.getTotalAmount()).thenReturn(Money.wons(50000));

        Payment mockPayment = Payment.initiate("order-123", Money.wons(50000), "BALANCE");
        when(paymentService.initiate("order-123", Money.wons(50000), "BALANCE")).thenReturn(mockPayment);

        // When
        PaymentResult result = paymentFacadeService.requestPayment(command);

        // Then
        assertThat(result.orderId()).isEqualTo("order-123");
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(50000));
        assertThat(result.method()).isEqualTo("BALANCE");

        verify(paymentService).process(command, mockOrder, mockPayment);
    }

    @Test
    @DisplayName("결제 확인 성공")
    void confirmPayment_success() {
        // Given
        Payment payment = spy(Payment.initiate("order-1001", Money.wons(10000), "BALANCE"));
        when(paymentService.getByPgTraansactionId("pg-123")).thenReturn(payment);

        Order order = mock(Order.class);
        when(orderService.getOrderForPayment("order-1001")).thenReturn(order);

        // Mock markSuccess 메서드의 동작을 정의
        doAnswer(invocation -> {
            payment.complete(); // 상태를 SUCCESS로 변경
            return null;
        }).when(paymentService).markSuccess(payment);

        // When
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand("pg-123"));

        // Then
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(paymentService).markSuccess(payment);
        verify(orderService).markConfirmed(order);
    }

    @Test
    @DisplayName("이미 완료된 결제는 재확인하지 않음")
    void confirmPayment_alreadyCompleted() {
        // Given
        Payment payment = Payment.initiate("order-2002", Money.wons(50000), "BALANCE");
        Order order = mock(Order.class);
        payment.complete(); // 이미 완료

        when(paymentService.getByPgTraansactionId("pg-already")).thenReturn(payment);
        when(orderService.getOrderForPayment("order-2002")).thenReturn(order);/**/

        // When
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand("pg-already"));

        // Then
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(paymentService, never()).markSuccess(payment);
        verify(orderService, never()).markConfirmed(order);
    }

}

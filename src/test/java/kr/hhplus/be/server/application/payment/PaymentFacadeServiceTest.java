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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
        when(paymentService.initiate(any(), any(), any())).thenReturn(mockPayment);

        // ✅ 먼저 설정
        when(paymentService.process(any(), any(), any())).thenReturn(true);

        // When
        PaymentResult result = paymentFacadeService.requestPayment(command);

        // Then
        assertThat(result.orderId()).isEqualTo("order-123");
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(50000));
        assertThat(result.method()).isEqualTo("BALANCE");

        verify(paymentService).process(eq(command), eq(mockOrder), eq(mockPayment));
        verify(paymentService).markSuccess(mockPayment);
        verify(orderService).markConfirmed(mockOrder);
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
        when(paymentService.process(any(), any(), any())).thenReturn(true);

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
        payment.complete(); // 이미 완료

        when(paymentService.getByPgTraansactionId("pg-already")).thenReturn(payment);

        // When
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand("pg-already"));

        // Then
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(paymentService, never()).markSuccess(any());
        verify(orderService, never()).markConfirmed(any());
    }
    @Test
    @DisplayName("잔액 부족 등으로 결제 실패 시 예외 발생")
    void requestPayment_fail_whenBalanceInsufficient() {
        // Given
        RequestPaymentCommand command = new RequestPaymentCommand("order-999", 1L, "BALANCE");

        Order order = mock(Order.class);
        when(orderService.getOrderForPayment("order-999")).thenReturn(order);
        when(order.getTotalAmount()).thenReturn(Money.wons(30000));

        Payment payment = Payment.initiate("order-999", Money.wons(30000), "BALANCE");
        when(paymentService.initiate(any(), any(), any())).thenReturn(payment);
        when(paymentService.process(any(), any(), any())).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> paymentFacadeService.requestPayment(command))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잔액이 부족");

        verify(paymentService).markFailure(payment);
    }

}

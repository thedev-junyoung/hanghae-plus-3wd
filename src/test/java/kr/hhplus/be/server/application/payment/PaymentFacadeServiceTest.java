package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.application.orderexport.OrderExportService;
import kr.hhplus.be.server.application.productstatistics.ProductStatisticsService;
import kr.hhplus.be.server.application.productstatistics.RecordSalesCommand;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.ExternalPaymentGateway;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentFacadeServiceTest {

    private PaymentService paymentService;
    private OrderService orderService;
    private BalanceService balanceService;
    private ExternalPaymentGateway externalGateway;
    private ProductStatisticsService productStatisticsService;
    private OrderExportService orderExportService;

    private PaymentFacadeService paymentFacadeService;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        orderService = mock(OrderService.class);
        balanceService = mock(BalanceService.class);
        externalGateway = mock(ExternalPaymentGateway.class);
        productStatisticsService = mock(ProductStatisticsService.class);
        orderExportService = mock(OrderExportService.class);

        paymentFacadeService = new PaymentFacadeService(
                paymentService, orderService, balanceService,
                externalGateway, productStatisticsService, orderExportService
        );
    }

    @Test
    @DisplayName("결제 요청 성공 및 통계 처리 포함")
    void requestPayment_success() {
        // Given
        RequestPaymentCommand command = new RequestPaymentCommand("order-123", 1L, "BALANCE");

        OrderItem item = OrderItem.of(1L, 2, 270, Money.wons(10000));
        Order mockOrder = mock(Order.class);
        when(mockOrder.getTotalAmount()).thenReturn(BigDecimal.valueOf(20000));
        when(mockOrder.getItems()).thenReturn(List.of(item));
        when(orderService.getOrderForPayment("order-123")).thenReturn(mockOrder);

        Payment mockPayment = Payment.initiate("order-123", Money.wons(20000), "BALANCE");
        when(paymentService.initiate("order-123", Money.wons(20000), "BALANCE")).thenReturn(mockPayment);

        when(externalGateway.requestPayment("order-123")).thenReturn(true);

        // When
        PaymentResult result = paymentFacadeService.requestPayment(command);

        // Then
        assertThat(result.orderId()).isEqualTo("order-123");
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(20000));
        assertThat(result.method()).isEqualTo("BALANCE");

        verify(paymentService).process(command, mockOrder, mockPayment);
        verify(productStatisticsService).record(
                new RecordSalesCommand(1L, 2, 20000L)
        );
        verify(orderExportService).export(any());
        verify(externalGateway, atMostOnce()).requestPayment("order-123");
    }

    @Test
    @DisplayName("결제 확인 성공")
    void confirmPayment_success() {
        // Given
        Payment payment = spy(Payment.initiate("order-1001", Money.wons(10000), "BALANCE"));
        when(paymentService.getByPgTraansactionId("pg-123")).thenReturn(payment);

        Order order = mock(Order.class);

        when(externalGateway.confirmPayment("pg-123")).thenReturn(true);

        when(orderService.getOrderForPayment("order-1001")).thenReturn(order);

        doAnswer(invocation -> {
            payment.complete();
            return null;
        }).when(paymentService).markSuccess(payment);

        // When
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand("pg-123"));

        // Then
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(paymentService).markSuccess(payment);
        verify(orderService).markConfirmed(order);
        verify(externalGateway, atMostOnce()).confirmPayment("pg-123");
    }

    @Test
    @DisplayName("이미 완료된 결제는 재확인하지 않음")
    void confirmPayment_alreadyCompleted() {
        // Given
        Payment payment = Payment.initiate("order-2002", Money.wons(50000), "BALANCE");
        payment.complete();

        Order order = mock(Order.class);
        when(paymentService.getByPgTraansactionId("pg-already")).thenReturn(payment);
        when(orderService.getOrderForPayment("order-2002")).thenReturn(order);

        // When
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand("pg-already"));

        // Then
        assertThat(result.status()).isEqualTo(PaymentStatus.SUCCESS);
        verify(paymentService, never()).markSuccess(payment);
        verify(orderService, never()).markConfirmed(order);
        verify(externalGateway, never()).confirmPayment("pg-already");
    }
}

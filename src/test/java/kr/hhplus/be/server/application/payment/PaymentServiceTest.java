package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.payment.exception.UnsupportedPaymentMethodException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private BalancePaymentProcessor balancePaymentProcessor;
    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        balancePaymentProcessor = mock(BalancePaymentProcessor.class);
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(balancePaymentProcessor, paymentRepository);
    }

    @Test
    @DisplayName("결제 생성 - initiate")
    void initiate_createsPayment() {
        // given
        String orderId = "order-1";
        Money amount = Money.wons(10000);
        String method = "BALANCE";

        // when
        Payment payment = paymentService.initiate(orderId, amount, method);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getOrderId()).isEqualTo(orderId);
        assertThat(payment.getAmount()).isEqualTo(amount);
        assertThat(payment.getStatus().name()).isEqualTo("INITIATED");

        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("결제 성공 처리 - markSuccess")
    void markSuccess_updatesPaymentToSuccess() {
        // given
        Payment payment = Payment.initiate("order-1", Money.wons(10000), "BALANCE");

        // when
        paymentService.markSuccess(payment);

        // then
        assertThat(payment.getStatus().name()).isEqualTo("SUCCESS");
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("결제 실패 처리 - markFailure")
    void markFailure_updatesPaymentToFailure() {
        // given
        Payment payment = Payment.initiate("order-1", Money.wons(10000), "BALANCE");

        // when
        paymentService.markFailure(payment);

        // then
        assertThat(payment.getStatus().name()).isEqualTo("FAILURE");
        verify(paymentRepository).save(payment);
    }

    @Test
    @DisplayName("결제 수단에 따라 Processor 위임 처리")
    void process_delegatesToProcessor() {
        // given
        RequestPaymentCommand command = new RequestPaymentCommand("order-123", 1L, "BALANCE");
        Order mockOrder = mock(Order.class);
        Payment payment = Payment.initiate("order-123", Money.wons(50000), "BALANCE");

        // when
        paymentService.process(command, mockOrder, payment);

        // then
        verify(balancePaymentProcessor).process(command, mockOrder, payment);
    }

    @Test
    @DisplayName("지원되지 않는 결제 수단 사용 시 예외 발생")
    void process_throwsExceptionForUnsupportedMethod() {
        // given
        RequestPaymentCommand command = new RequestPaymentCommand("order-123", 1L, "BITCOIN");
        Order mockOrder = mock(Order.class);
        Payment payment = Payment.initiate("order-123", Money.wons(50000), "BITCOIN");

        // then
        assertThatThrownBy(() -> paymentService.process(command, mockOrder, payment))
                .isInstanceOf(UnsupportedPaymentMethodException.class)
                .hasMessageContaining("지원되지 않는 결제 수단");

        verify(balancePaymentProcessor, never()).process(command, mockOrder, payment);    }
}

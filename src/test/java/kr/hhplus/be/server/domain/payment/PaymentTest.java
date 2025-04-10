package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.payment.exception.InvalidPaymentStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("결제 객체 생성 - 초기 상태는 INITIATED")
    void initiate_payment_success() {
        Payment payment = Payment.initiate("order-001", Money.wons(10000), "BALANCE");

        assertThat(payment.getOrderId()).isEqualTo("order-001");
        assertThat(payment.getAmount().value()).isEqualTo(BigDecimal.valueOf(10000));
        assertThat(payment.getMethod()).isEqualTo("BALANCE");
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.INITIATED);
        assertThat(payment.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("결제 성공 시 상태는 SUCCESS")
    void complete_payment_changes_status() {
        Payment payment = Payment.initiate("order-001", Money.wons(5000), "BALANCE");

        payment.complete();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.isCompleted()).isTrue();
    }

    @Test
    @DisplayName("결제 실패 시 상태는 FAILURE")
    void fail_payment_changes_status() {
        Payment payment = Payment.initiate("order-001", Money.wons(7000), "CARD");

        payment.fail();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILURE);
        assertThat(payment.isCompleted()).isFalse();
    }
    @Test
    @DisplayName("이미 SUCCESS 상태인 결제는 다시 성공 처리할 수 없다")
    void complete_payment_shouldFail_ifAlreadyCompleted() {
        Payment payment = Payment.initiate("order-001", Money.wons(5000), "BALANCE");
        payment.complete();

        assertThatThrownBy(payment::complete)
                .isInstanceOf(InvalidPaymentStateException.class)
                .hasMessageContaining("이미 완료되었거나 실패/취소된 결제입니다.");
    }

    @Test
    @DisplayName("SUCCESS 상태인 결제는 실패 처리할 수 없다")
    void fail_payment_shouldFail_ifAlreadyCompleted() {
        Payment payment = Payment.initiate("order-001", Money.wons(5000), "BALANCE");
        payment.complete();

        assertThatThrownBy(payment::fail)
                .isInstanceOf(InvalidPaymentStateException.class)
                .hasMessageContaining("실패 처리할 수 없는 결제 상태입니다.");
    }
}

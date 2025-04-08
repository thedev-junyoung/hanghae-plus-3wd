package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.payment.exception.InvalidPaymentStateException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Payment {
    private final String id;
    private final String orderId;
    private final Money amount;
    private PaymentStatus status;
    private final String method;
    private final LocalDateTime createdAt;

    private Payment(String id, String orderId, Money amount, PaymentStatus status, String method, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.createdAt = createdAt;
    }

    public static Payment initiate(String orderId, Money amount, String method) {
        return new Payment(
                java.util.UUID.randomUUID().toString(),
                orderId,
                amount,
                PaymentStatus.INITIATED,
                method,
                LocalDateTime.now()
        );
    }

    public void complete() {
        if (!status.canMarkSuccess()) {
            throw new InvalidPaymentStateException(status, "이미 완료되었거나 실패/취소된 결제입니다.");
        }
        this.status = PaymentStatus.SUCCESS;
    }

    public void fail() {
        if (!status.canMarkFailure()) {
            throw new InvalidPaymentStateException(status, "실패 처리할 수 없는 결제 상태입니다.");
        }
        this.status = PaymentStatus.FAILURE;
    }

    public boolean isCompleted() {
        return status == PaymentStatus.SUCCESS;
    }

}

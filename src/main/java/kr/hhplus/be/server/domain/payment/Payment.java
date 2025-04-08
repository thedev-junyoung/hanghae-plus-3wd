package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.payment.exception.InvalidPaymentStateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    private String id;

    @Column(nullable = false)
    private String orderId;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

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
                UUID.randomUUID().toString(),
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

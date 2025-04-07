package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.common.vo.Money;
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
        this.status = PaymentStatus.SUCCESS;
    }

    public void fail() {
        this.status = PaymentStatus.FAILURE;
    }

    public boolean isCompleted() {
        return status == PaymentStatus.SUCCESS;
    }

}

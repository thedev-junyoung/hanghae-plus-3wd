package kr.hhplus.be.server.infrastructure.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
public class PaymentEntity {

    @Id
    private String id;

    private String orderId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String method;

    private LocalDateTime createdAt;

    public static PaymentEntity from(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.id = payment.getId();
        entity.orderId = payment.getOrderId();
        entity.amount = payment.getAmount().value();
        entity.status = payment.getStatus();
        entity.method = payment.getMethod();
        entity.createdAt = payment.getCreatedAt();
        return entity;
    }

    public Payment toDomain() {
        return Payment.initiate(
                orderId,
                Money.wons(amount),
                method
        );
    }
}

package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResult(
        String paymentId,
        String orderId,
        long amount,
        String method,
        PaymentStatus status,
        LocalDateTime createdAt
) {
    public static PaymentResult from(kr.hhplus.be.server.domain.payment.Payment payment) {
        return new PaymentResult(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}

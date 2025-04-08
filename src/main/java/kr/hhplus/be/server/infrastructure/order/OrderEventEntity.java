package kr.hhplus.be.server.infrastructure.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderEventEntity {
    @Id
    private Long id;
    private String aggregateType; // ORDER
    private String eventType;     // ORDER_CREATED, PAYMENT_COMPLETED 등
    private String payload;       // JSON 직렬화된 데이터
    private String status;        // PENDING, SENT 등
    private String createdAt;     // LocalDateTime 직렬화된 데이터

}

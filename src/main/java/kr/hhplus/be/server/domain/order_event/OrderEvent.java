package kr.hhplus.be.server.domain.order_event;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderEvent {
    private UUID id;
    private String aggregateType; // ORDER
    private String eventType;     // ORDER_CREATED, PAYMENT_COMPLETED 등
    private String payload;       // JSON 직렬화된 데이터
    private EventStatus status;   // PENDING, SENT 등
    private LocalDateTime createdAt;
}

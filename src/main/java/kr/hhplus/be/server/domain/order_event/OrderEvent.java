package kr.hhplus.be.server.domain.order_event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.order.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OrderEvent {

    private final UUID id;
    private final String aggregateType; // "ORDER"
    private final String eventType;     // "ORDER_CREATED", "PAYMENT_COMPLETED" 등
    private final String payload;       // 직렬화된 JSON 문자열
    private final EventStatus status;   // PENDING, SENT 등
    private final LocalDateTime createdAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private OrderEvent(UUID id, String aggregateType, String eventType, String payload, EventStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static OrderEvent paymentCompleted(Order order) {
        return new OrderEvent(
                UUID.randomUUID(),
                "ORDER",
                "PAYMENT_COMPLETED",
                toJson(order),
                EventStatus.PENDING,
                LocalDateTime.now()
        );
    }

    private static String toJson(Order order) {
        try {
            return objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Order 직렬화 실패", e);
        }
    }
}

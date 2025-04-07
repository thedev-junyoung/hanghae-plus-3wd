package kr.hhplus.be.server.infrastructure.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    private Long userId;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    public static OrderEntity from(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId();
        entity.userId = order.getUserId();
        entity.totalAmount = order.getTotalAmount().value();
        entity.status = order.getStatus();
        entity.createdAt = order.getCreatedAt();
        return entity;
    }

    public static Order toDamain(OrderEntity order) {
        return Order.create(
                order.id,
                order.userId,
                null, // OrderItem은 별도로 처리해야 하므로 null로 설정
                Money.wons(order.totalAmount)
        );
    }

    public Order toDomain(List<OrderItem> orderItems) {
        return Order.create(
                id,
                userId,
                orderItems,
                Money.wons(totalAmount)
        );
    }
}


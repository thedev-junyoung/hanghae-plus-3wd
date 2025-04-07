package kr.hhplus.be.server.infrastructure.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    private Long productId;

    private int quantity;

    private int size;

    private BigDecimal price;

    public static OrderItemEntity from(OrderItem item, String orderId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.orderId = orderId;
        entity.productId = item.getProductId();
        entity.quantity = item.getQuantity();
        entity.size = item.getSize();
        entity.price = item.getPrice().value();
        return entity;
    }

    public OrderItem toDomain() {
        return OrderItem.of(productId, quantity, size, Money.wons(price));
    }
}

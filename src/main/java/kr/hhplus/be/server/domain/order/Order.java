package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.exception.InvalidOrderStateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

 
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    private String id;  // UUID or 외부에서 주입하는 key라고 가정

    @Column(nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @Column(name = "total_amount", nullable = false)
    private long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Order create(String id, Long userId, List<OrderItem> items, Money totalAmount) {
        Order order = new Order();
        order.id = id;
        order.userId = userId;
        order.items = items;
        order.totalAmount = totalAmount.value(); // BigDecimal 값만 저장
        order.status = OrderStatus.CREATED;
        order.createdAt = LocalDateTime.now();

        // setter 없이 양방향 연관관계 유지
        if (items != null) {
            for (OrderItem item : items) {
                item.initOrder(order);
            }
        }

        return order;
    }

    public void cancel() {
        if (!status.canCancel()) {
            throw new InvalidOrderStateException(status, "cancel()");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void markConfirmed() {
        if (!status.canConfirm()) {
            throw new InvalidOrderStateException(status, "markConfirmed()");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void validatePayable() {
        if (status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(status, "payment");
        }
    }
  
}

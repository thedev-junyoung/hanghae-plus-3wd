package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.exception.InvalidOrderStateException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Order {

    private final String id;
    private final Long userId;
    private final List<OrderItem> items;
    private final Money totalAmount;
    private OrderStatus status;
    private final LocalDateTime createdAt;

    private Order(String id, Long userId, List<OrderItem> items, Money totalAmount, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Order create(String id, Long userId, List<OrderItem> items, Money totalAmount) {
        return new Order(id, userId, items, totalAmount, OrderStatus.CREATED, LocalDateTime.now());
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

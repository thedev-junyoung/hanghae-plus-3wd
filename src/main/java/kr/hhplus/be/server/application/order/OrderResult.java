package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResult(
        String orderId,
        Long userId,
        List<OrderItemResult> items,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
    public static OrderResult from(Order order) {
        List<OrderItemResult> itemResults = order.getItems().stream()
                .map(OrderItemResult::from)
                .toList();

        return new OrderResult(
                order.getId(),
                order.getUserId(),
                itemResults,
                order.getTotalAmount().value(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    public record OrderItemResult(
            Long productId,
            int quantity,
            int size,
            BigDecimal price
    ) {
        public static OrderItemResult from(OrderItem item) {
            return new OrderItemResult(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getSize(),
                    item.getPrice().value()
            );
        }
    }
}

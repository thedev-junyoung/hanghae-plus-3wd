package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.infrastructure.order.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Long userId, List<OrderItem> items, Money totalAmount) {
        String orderId = UUID.randomUUID().toString();
        Order order = Order.create(orderId, userId, items, totalAmount);
        orderRepository.save(order);
        return order;
    }

    public Order getOrderForPayment(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("결제는 CREATED 상태의 주문만 가능합니다.");
        }
        return order;
    }
    public void markConfirmed(Order order) {
        order.markConfirmed();
        orderRepository.save(order);
    }
}

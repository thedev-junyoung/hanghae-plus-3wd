package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
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
        orderRepository.save(OrderEntity.from(order));
        return order;
    }
}

package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderException;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items, Money totalAmount) {
        String orderId = UUID.randomUUID().toString();
        Order order = Order.create(orderId, userId, items, totalAmount);
        orderRepository.save(order);
        return order;
    }

    public Order getOrderForPayment(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException.NotFoundException(orderId));

        order.validatePayable();
        return order;
    }

    @Transactional
    public void markConfirmed(Order order) {
        order.markConfirmed();
        orderRepository.save(order);
    }
}

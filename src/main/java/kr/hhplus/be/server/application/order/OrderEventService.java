package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order_event.OrderEvent;
import kr.hhplus.be.server.domain.order_event.OrderEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventService implements OrderEventUseCase {

    private final OrderEventRepository repository;

    @Override
    public void recordPaymentCompletedEvent(Order order) {
        OrderEvent event = OrderEvent.paymentCompleted(order);
        repository.save(event);
    }
}

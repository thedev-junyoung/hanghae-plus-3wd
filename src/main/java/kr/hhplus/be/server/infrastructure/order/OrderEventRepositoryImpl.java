package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order_event.OrderEvent;
import kr.hhplus.be.server.domain.order_event.OrderEventRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class OrderEventRepositoryImpl implements OrderEventRepository {
    @Override
    public void save(OrderEvent event) {

    }

    @Override
    public Optional<OrderEvent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<OrderEvent> findPendingEvents() {
        return List.of();
    }

    @Override
    public void markAsSent(UUID id) {

    }
}

package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OrderRepository{
    void save(Order order);
    Optional<Order> findById(String orderId);

}

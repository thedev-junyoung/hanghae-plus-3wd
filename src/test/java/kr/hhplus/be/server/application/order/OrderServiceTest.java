package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.infrastructure.order.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    @DisplayName("주문 객체를 생성하고 저장소에 저장한다")
    void createOrder_shouldCreateAndSaveOrder() {
        // Given
        Long userId = 1L;
        List<OrderItem> items = List.of(
                OrderItem.of(10L, 2, 270, Money.wons(100000))
        );
        Money totalAmount = Money.wons(200000);

        // When
        Order order = orderService.createOrder(userId, items, totalAmount);

        // Then
        assertThat(order).isNotNull();
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);

        verify(orderRepository).save(any(OrderEntity.class));
    }
}

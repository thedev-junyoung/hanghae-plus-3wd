package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.order.exception.InvalidOrderStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    @DisplayName("정상적으로 주문을 생성할 수 있다")
    void createOrder_shouldSucceed() {
        List<OrderItem> items = List.of(
                OrderItem.of(1L, 1, 270, Money.wons(100000))
        );

        Order order = Order.create("order-id", 1L, items, Money.wons(100000));

        assertThat(order.getId()).isEqualTo("order-id");
        assertThat(order.getUserId()).isEqualTo(1L);
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getTotalAmount()).isEqualTo(Money.wons(100000));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("생성된 주문은 취소할 수 있다")
    void cancelOrder_shouldChangeStatusToCancelled() {
        Order order = Order.create("order-id", 1L,
                List.of(OrderItem.of(1L, 1, 270, Money.wons(100000))),
                Money.wons(100000));

        order.cancel();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("이미 취소된 주문은 다시 취소할 수 없다")
    void cancelOrder_shouldFailIfNotCreatedStatus() {
        Order order = Order.create("order-id", 1L,
                List.of(OrderItem.of(1L, 1, 270, Money.wons(100000))),
                Money.wons(100000));

        order.cancel(); // 상태를 CANCELLED로 전환

        assertThrows(InvalidOrderStateException.class, order::cancel);
    }

    @Test
    @DisplayName("생성된 주문은 CONFIRMED 상태로 변경할 수 있다")
    void markOrderAsConfirmed_shouldChangeStatusToConfirmed() {
        Order order = Order.create("order-id", 1L,
                List.of(OrderItem.of(1L, 1, 270, Money.wons(100000))),
                Money.wons(100000));

        order.markConfirmed();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("이미 CONFIRMED 상태인 주문은 다시 CONFIRMED로 변경할 수 없다")
    void markConfirmed_shouldFailIfNotCreatedStatus() {
        Order order = Order.create("order-id", 1L,
                List.of(OrderItem.of(1L, 1, 270, Money.wons(100000))),
                Money.wons(100000));

        order.markConfirmed(); // CONFIRMED 상태

        assertThrows(InvalidOrderStateException.class, order::markConfirmed);
    }
}

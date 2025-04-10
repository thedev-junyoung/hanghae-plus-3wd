package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderFacadeServiceTest {

    ProductService productService = mock(ProductService.class);
    BalanceService balanceService = mock(BalanceService.class);
    OrderService orderService = mock(OrderService.class);
    OrderEventService orderEventService = mock(OrderEventService.class);

    OrderFacadeService sut = new OrderFacadeService(
            productService,
            balanceService,
            orderService,
            orderEventService
    );

    @Test
    @DisplayName("주문 생성 시, 주문 저장과 이벤트 저장이 함께 호출된다")
    void createOrder_should_save_order_and_emit_event() {
        // given
        Long userId = 1L;
        Long productId = 10L;
        int quantity = 2;
        int size = 260;
        long price = 5000L;

        CreateOrderCommand.OrderItemCommand itemCommand =
                new CreateOrderCommand.OrderItemCommand(productId, quantity, size);
        CreateOrderCommand command = new CreateOrderCommand(userId, List.of(itemCommand));

        ProductDetailResult productDetailResult = new ProductDetailResult(
                new ProductInfo(productId, "티셔츠", price, 10)
        );

        Order fakeOrder = mock(Order.class);
        when(fakeOrder.getUserId()).thenReturn(userId);
        when(fakeOrder.getId()).thenReturn("ORD-1234");
        when(fakeOrder.getItems()).thenReturn(List.of(
                OrderItem.of(productId, quantity, size, Money.wons(price))
        ));
        when(fakeOrder.getTotalAmount()).thenReturn(price * quantity);

        when(productService.getProductDetail(any())).thenReturn(productDetailResult);
        when(orderService.createOrder(eq(userId), any(), any())).thenReturn(fakeOrder);

        // when
        OrderResult result = sut.createOrder(command);

        // then
        verify(productService).decreaseStock(any(DecreaseStockCommand.class));
        verify(balanceService).decreaseBalance(any(DecreaseBalanceCommand.class));
        verify(orderService).createOrder(eq(userId), any(), any());
        verify(orderEventService).recordPaymentCompletedEvent(fakeOrder);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
    }
}

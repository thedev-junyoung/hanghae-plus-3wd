package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import kr.hhplus.be.server.domain.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OrderFacadeServiceTest {

    private ProductService productService;
    private BalanceService balanceService;
    private OrderService orderService;

    private OrderFacadeService orderFacadeService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        balanceService = mock(BalanceService.class);
        orderService = mock(OrderService.class);
        orderFacadeService = new OrderFacadeService(productService, balanceService, orderService);
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_shouldCreateOrderSuccessfully() {
        // Given
        CreateOrderCommand.OrderItemCommand item1 = new CreateOrderCommand.OrderItemCommand(1L, 2, 270);
        CreateOrderCommand.OrderItemCommand item2 = new CreateOrderCommand.OrderItemCommand(2L, 1, 280);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item1, item2));

        ProductResult product1 = new ProductResult(1L, "Nike Air", BigDecimal.valueOf(100000), 10, null, null, null, null);
        ProductResult product2 = new ProductResult(2L, "Adidas Ultra", BigDecimal.valueOf(120000), 5, null, null, null, null);
        when(productService.getProductDetail(any())).thenReturn(
                new ProductDetailResult(product1),
                new ProductDetailResult(product2)
        );

        Order mockOrder = Order.create("order-id", 100L,
                List.of(
                        OrderItem.of(1L, 2, 270, Money.wons(100000)),
                        OrderItem.of(2L, 1, 280, Money.wons(120000))
                ),
                Money.wons(320000));
        when(orderService.createOrder(any(), any(), any())).thenReturn(mockOrder);

        // When
        OrderResult result = orderFacadeService.createOrder(command);

        // Then
        assertThat(result.orderId()).isEqualTo("order-id");
        assertThat(result.userId()).isEqualTo(100L);
        assertThat(result.totalAmount()).isEqualTo(BigDecimal.valueOf(320000));
        assertThat(result.items()).hasSize(2);

        verify(productService, times(2)).decreaseStock(any());
        verify(orderService).createOrder(eq(100L), any(), eq(Money.wons(320000)));
        verify(balanceService).decreaseBalance(any());

    }
    @Test
    @DisplayName("재고 부족 시 주문 생성 실패")
    void createOrder_shouldFail_whenStockInsufficient() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(1L, 5, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        ProductResult product = new ProductResult(1L, "Nike Air", BigDecimal.valueOf(100000), 2, null, null, null, null);
        when(productService.getProductDetail(any())).thenReturn(new ProductDetailResult(product));

        // decreaseStock 호출 시 예외 발생
        doThrow(new InsufficientStockException())
                .when(productService)
                .decreaseStock(any());

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(InsufficientStockException.class);

        verify(productService).getProductDetail(any());
        verify(productService).decreaseStock(any());
        verifyNoInteractions(orderService); // 주문 저장은 실행되지 않아야 함
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문 시 실패")
    void createOrder_shouldFail_whenProductNotFound() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(999L, 1, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        when(productService.getProductDetail(any()))
                .thenThrow(new ProductNotFoundException(999L));

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productService).getProductDetail(any());
        verify(productService, never()).decreaseStock(any());
        verifyNoInteractions(orderService);
    }
}
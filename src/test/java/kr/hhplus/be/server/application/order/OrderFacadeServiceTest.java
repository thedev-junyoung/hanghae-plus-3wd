package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import kr.hhplus.be.server.domain.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderFacadeServiceTest {

    private ProductService productService;
    private BalanceService balanceService;
    private OrderService orderService;

    private OrderFacadeService orderFacadeService;

    @Captor
    ArgumentCaptor<List<OrderItem>> orderItemsCaptor;

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

        when(productService.getProductDetail(new GetProductDetailCommand(1L))).thenReturn(new ProductDetailResult(product1));
        when(productService.getProductDetail(new GetProductDetailCommand(2L))).thenReturn(new ProductDetailResult(product2));

        // mockOrder는 추후 orderItemsCaptor 사용 후 만들어야 정확한 item 참조 가능
        when(orderService.createOrder(eq(100L), anyList(), eq(Money.wons(320000))))
                .thenAnswer(invocation -> {
                    List<OrderItem> capturedItems = invocation.getArgument(1);
                    return Order.create("order-id", 100L, capturedItems, Money.wons(320000));
                });

        // When
        OrderResult result = orderFacadeService.createOrder(command);

        // Then
        assertThat(result.orderId()).isEqualTo("order-id");
        assertThat(result.userId()).isEqualTo(100L);
        assertThat(result.totalAmount()).isEqualTo(BigDecimal.valueOf(320000));
        assertThat(result.items()).hasSize(2);
        assertThat(result.items()).extracting("productId").containsExactlyInAnyOrder(1L, 2L);

        verify(productService).decreaseStock(new DecreaseStockCommand(1L, 260,2));
        verify(productService).decreaseStock(new DecreaseStockCommand(2L, 260,1));
        verify(orderService).createOrder(eq(100L), orderItemsCaptor.capture(), eq(Money.wons(320000)));

        List<OrderItem> captured = orderItemsCaptor.getValue();
        assertThat(captured).hasSize(2);
        assertThat(captured).extracting(OrderItem::getProductId).containsExactlyInAnyOrder(1L, 2L);
    }


    @Test
    @DisplayName("재고 부족 시 주문 생성 실패")
    void createOrder_shouldFail_whenStockInsufficient() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(1L, 5, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        ProductResult product = new ProductResult(1L, "Nike Air", BigDecimal.valueOf(100000), 2, null, null, null, null);
        when(productService.getProductDetail(new GetProductDetailCommand(1L)))
                .thenReturn(new ProductDetailResult(product));

        doThrow(new InsufficientStockException())
                .when(productService).decreaseStock(new DecreaseStockCommand(1L, 260,5));

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(InsufficientStockException.class);

        verify(productService).getProductDetail(new GetProductDetailCommand(1L));
        verify(productService).decreaseStock(new DecreaseStockCommand(1L, 260,5));
        verifyNoInteractions(orderService);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문 시 실패")
    void createOrder_shouldFail_whenProductNotFound() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(999L, 1, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        when(productService.getProductDetail(new GetProductDetailCommand(999L)))
                .thenThrow(new ProductNotFoundException(999L));

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productService).getProductDetail(new GetProductDetailCommand(999L));
        verify(productService, never()).decreaseStock(new DecreaseStockCommand(999L,260, 1));
        verifyNoInteractions(orderService);
    }
}

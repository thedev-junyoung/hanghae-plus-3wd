package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.product.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

 
import java.time.LocalDate;
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
    void createOrder_success() {
        // given
        CreateOrderCommand.OrderItemCommand item1 = new CreateOrderCommand.OrderItemCommand(1L, 2, 270);
        CreateOrderCommand.OrderItemCommand item2 = new CreateOrderCommand.OrderItemCommand(2L, 1, 280);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item1, item2));

        Product product1 = Product.create("Nike Air", "Nike", Money.wons(100_000), LocalDate.now(), null, null);
        Product product2 = Product.create("Adidas Boost", "Adidas", Money.wons(120_000), LocalDate.now(), null, null);
        ProductInfo info1 = ProductInfo.from(product1, 10);
        ProductInfo info2 = ProductInfo.from(product2, 5);

        when(productService.getProductDetail(new GetProductDetailCommand(1L)))
                .thenReturn(ProductDetailResult.from(info1));
        when(productService.getProductDetail(new GetProductDetailCommand(2L)))
                .thenReturn(ProductDetailResult.from(info2));

        when(orderService.createOrder(eq(100L), anyList(), eq(Money.wons(320_000))))
                .thenAnswer(invocation -> {
                    List<OrderItem> items = invocation.getArgument(1);
                    return Order.create("order-id", 100L, items, Money.wons(320_000));
                });

        // when
        OrderResult result = orderFacadeService.createOrder(command);

        // then
        assertThat(result.orderId()).isEqualTo("order-id");
        assertThat(result.totalAmount()).isEqualTo(320_000L);

        verify(productService).decreaseStock(new DecreaseStockCommand(1L, 270, 2));
        verify(productService).decreaseStock(new DecreaseStockCommand(2L, 280, 1));
        verify(balanceService).decreaseBalance(new DecreaseBalanceCommand(100L, 320_000L));
    }

    @Test
    @DisplayName("재고 부족 시 주문 생성 실패")
    void createOrder_shouldFail_whenStockInsufficient() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(1L, 5, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        Product product = Product.create("Nike Air", "Nike", Money.wons(100000L), LocalDate.now(), null, null);
        ProductInfo info = ProductInfo.from(product, 1);

        when(productService.getProductDetail(new GetProductDetailCommand(1L)))
                .thenReturn(ProductDetailResult.from(info));

        doThrow(new ProductException.InsufficientStockException("재고가 부족합니다."))
                .when(productService).decreaseStock(new DecreaseStockCommand(1L, 270, 5));

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(ProductException.InsufficientStockException.class);

        verify(productService).getProductDetail(new GetProductDetailCommand(1L));
        verify(productService).decreaseStock(new DecreaseStockCommand(1L, 270, 5));
        verifyNoInteractions(balanceService);
        verifyNoInteractions(orderService);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문 시 실패")
    void createOrder_shouldFail_whenProductNotFound() {
        // Given
        CreateOrderCommand.OrderItemCommand item = new CreateOrderCommand.OrderItemCommand(999L, 1, 270);
        CreateOrderCommand command = new CreateOrderCommand(100L, List.of(item));

        when(productService.getProductDetail(new GetProductDetailCommand(999L)))
                .thenThrow(new ProductException.NotFoundException(999L));

        // When & Then
        assertThatThrownBy(() -> orderFacadeService.createOrder(command))
                .isInstanceOf(ProductException.NotFoundException.class);

        verify(productService).getProductDetail(new GetProductDetailCommand(999L));
        verify(productService, never()).decreaseStock(new DecreaseStockCommand(999L,260, 1));
        verifyNoInteractions(orderService);
    }
}

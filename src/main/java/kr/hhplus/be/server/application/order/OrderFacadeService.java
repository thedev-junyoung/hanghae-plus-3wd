package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.common.vo.Money;
import org.springframework.transaction.annotation.Transactional; // ✅ 정답
import kr.hhplus.be.server.application.balance.BalanceService;
import kr.hhplus.be.server.application.balance.DecreaseBalanceCommand;
import kr.hhplus.be.server.application.product.DecreaseStockCommand;
import kr.hhplus.be.server.application.product.GetProductDetailCommand;
import kr.hhplus.be.server.application.product.ProductDetailResult;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFacadeService implements OrderUseCase {

    private final ProductService productService;
    private final BalanceService balanceService;
    private final OrderService orderService;

    @Transactional
    @Override
    public OrderResult createOrder(CreateOrderCommand command) {
        Money total = Money.wons(0L);
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderCommand.OrderItemCommand item : command.items()) {
            ProductDetailResult product = productService.getProductDetail(
                    new GetProductDetailCommand(item.productId())
            );

            productService.decreaseStock(
                    new DecreaseStockCommand(item.productId(), item.size(),item.quantity())
            );

            Money itemPrice = Money.wons(product.product().price());
            Money itemTotal = itemPrice.multiply(item.quantity());

            orderItems.add(OrderItem.of(item.productId(), item.quantity(), item.size(), itemPrice));
            total = total.add(itemTotal);
        }
        balanceService.decreaseBalance(
                new DecreaseBalanceCommand(command.userId(), total.value())
        );
        Order order = orderService.createOrder(command.userId(), orderItems, total);

        return OrderResult.from(order);
    }
}

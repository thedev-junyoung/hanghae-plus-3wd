package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.vo.Money;
import org.springframework.transaction.annotation.Transactional;
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
    private final OrderService orderService;
    private final OrderEventService orderEventService;

    @Transactional
    @Override
    public OrderResult createOrder(CreateOrderCommand command) {
        // 1. 초기화 - 전체 금액 및 주문 아이템 리스트 준비
        Money total = Money.wons(0L);
        List<OrderItem> orderItems = new ArrayList<>();

        // 2. 각 상품에 대해 주문 상세 구성
        for (CreateOrderCommand.OrderItemCommand item : command.items()) {
            // 2-1. 상품 상세 조회 (가격 포함)
            ProductDetailResult product = productService.getProductDetail(
                    new GetProductDetailCommand(item.productId())
            );

            // 2-2. 상품 재고 차감
            productService.decreaseStock(
                    new DecreaseStockCommand(item.productId(), item.size(), item.quantity())
            );

            // 2-3. 주문 상품 가격 계산
            Money itemPrice = Money.wons(product.product().price());
            Money itemTotal = itemPrice.multiply(item.quantity());

            // 2-4. 주문 아이템으로 추가
            orderItems.add(OrderItem.of(item.productId(), item.quantity(), item.size(), itemPrice));
            total = total.add(itemTotal);
        }



        // 3. 주문 생성 및 저장
        Order order = orderService.createOrder(command.userId(), orderItems, total);

        // 4. 결제 완료 이벤트 발행 (Outbox 패턴 기반 처리)
        orderEventService.recordPaymentCompletedEvent(order);

        // 5. 응답 객체 반환
        return OrderResult.from(order);
    }
}


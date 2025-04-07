package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacadeService;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.common.dto.response.CustomApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderAPI {

    private final OrderFacadeService orderFacadeService;

    @Override
    public ResponseEntity<CustomApiResponse<Response.OrderResponse>> createOrder(Request.CreateOrderRequest request) {
        CreateOrderCommand command = request.toCommand();
        OrderResult result = orderFacadeService.createOrder(command);
        return ResponseEntity.ok(CustomApiResponse.success(Response.OrderResponse.from(result)));
    }

    // ========== NESTED DTO ==========

    public static class Request {

        @Getter
        @AllArgsConstructor
        public static class CreateOrderRequest {
            private Long userId;
            private List<OrderItemRequest> items;

            public CreateOrderCommand toCommand() {
                List<CreateOrderCommand.OrderItemCommand> itemCommands = items.stream()
                        .map(i -> new CreateOrderCommand.OrderItemCommand(i.productId, i.quantity, i.size))
                        .toList();
                return new CreateOrderCommand(userId, itemCommands);
            }
        }

        @Getter
        @AllArgsConstructor
        public static class OrderItemRequest {
            private Long productId;
            private int quantity;
            private int size;
        }
    }

    public static class Response {

        @Getter
        @AllArgsConstructor
        public static class OrderResponse {
            private String orderId;
            private Long userId;
            private List<OrderItemResponse> items;
            private BigDecimal totalAmount;
            private String status;

            public static OrderResponse from(OrderResult result) {
                List<OrderItemResponse> itemResponses = result.items().stream()
                        .map(OrderItemResponse::from)
                        .toList();

                return new OrderResponse(
                        result.orderId(),
                        result.userId(),
                        itemResponses,
                        result.totalAmount(),
                        result.status().name()
                );
            }

            @Getter
            @AllArgsConstructor
            public static class OrderItemResponse {
                private Long productId;
                private int quantity;
                private int size;
                private BigDecimal price;

                public static OrderItemResponse from(OrderResult.OrderItemResult item) {
                    return new OrderItemResponse(
                            item.productId(),
                            item.quantity(),
                            item.size(),
                            item.price()
                    );
                }
            }
        }
    }
}

package kr.hhplus.be.server.application.order;

public interface OrderUseCase {
    OrderResult createOrder(CreateOrderCommand command);
}

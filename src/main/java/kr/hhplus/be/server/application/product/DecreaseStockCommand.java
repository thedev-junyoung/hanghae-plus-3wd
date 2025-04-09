package kr.hhplus.be.server.application.product;

public record DecreaseStockCommand(
        Long productId,
        int size,
        int quantity
) {

}

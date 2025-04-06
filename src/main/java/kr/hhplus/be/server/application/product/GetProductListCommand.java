package kr.hhplus.be.server.application.product;

public record GetProductListCommand(
        int page,
        int size,
        String sort
) {

}

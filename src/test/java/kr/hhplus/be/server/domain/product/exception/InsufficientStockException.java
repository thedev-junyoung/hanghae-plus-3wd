package kr.hhplus.be.server.domain.product.exception;


public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super("재고가 부족합니다.");
    }
}
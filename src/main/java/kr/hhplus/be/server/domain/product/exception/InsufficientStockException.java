package kr.hhplus.be.server.domain.product.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super("Not enough stock.");
    }
}
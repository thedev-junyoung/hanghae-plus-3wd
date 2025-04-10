package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.common.exception.ErrorCode;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super(ErrorCode.INSUFFICIENT_STOCK.getMessage()); // ✅ 실제 에러 메시지 반환
    }
}
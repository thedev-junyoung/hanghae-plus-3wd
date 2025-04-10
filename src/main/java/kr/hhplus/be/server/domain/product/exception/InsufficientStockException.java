package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String message) {
        super(ErrorCode.INSUFFICIENT_STOCK, message);
    }
    public InsufficientStockException() {
        super(ErrorCode.INSUFFICIENT_STOCK);
    }
}
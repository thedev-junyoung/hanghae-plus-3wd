package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class ProductOutOfStockException extends BusinessException {
    public ProductOutOfStockException(Long productId) {
        super(ErrorCode.INSUFFICIENT_STOCK, "재고가 부족한 상품입니다. (상품 ID: " + productId + ")");
    }
}
package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class ProductNotReleasedException extends BusinessException {
    public ProductNotReleasedException(Long productId) {
        super(ErrorCode.INVALID_INPUT_VALUE, "아직 출시되지 않은 상품입니다. (상품 ID: " + productId + ")");
    }
}
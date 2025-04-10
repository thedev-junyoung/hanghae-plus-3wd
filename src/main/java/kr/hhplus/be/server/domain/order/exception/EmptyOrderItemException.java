package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class EmptyOrderItemException extends BusinessException {
    public EmptyOrderItemException() {
        super(ErrorCode.INVALID_PARAMETER, "주문 항목은 비어 있을 수 없습니다.");
    }
}
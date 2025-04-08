package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.order.OrderStatus;

public class InvalidOrderStateException extends BusinessException {

    public InvalidOrderStateException(OrderStatus currentStatus, String reason) {
        super(ErrorCode.INVALID_ORDER_STATUS,
                "현재 상태(" + currentStatus + ")에서는 이 작업을 수행할 수 없습니다: " + reason);
    }
}

package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(String orderId) {
        super(ErrorCode.ORDER_NOT_FOUND, "찾을 수없는 주문 ID: " + orderId);
    }
}

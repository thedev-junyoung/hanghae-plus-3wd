package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class InvalidTotalAmountException extends BusinessException {
    public InvalidTotalAmountException(long expected, long actual) {
        super(ErrorCode.INVALID_AMOUNT, "총 금액이 올바르지 않습니다. expected=" + expected + ", actual=" + actual);
    }
}

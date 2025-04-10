package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class MismatchedPaymentAmountException extends BusinessException {
    public MismatchedPaymentAmountException(long expected, long actual) {
        super(ErrorCode.INVALID_AMOUNT, "결제 금액이 올바르지 않습니다. expected=" + expected + ", actual=" + actual);
    }
}
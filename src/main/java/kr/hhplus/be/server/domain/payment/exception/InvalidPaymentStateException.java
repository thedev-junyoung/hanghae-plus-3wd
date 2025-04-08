package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.payment.PaymentStatus;

public class InvalidPaymentStateException extends BusinessException {
    public InvalidPaymentStateException(PaymentStatus status, String s) {
        super(ErrorCode.INVALID_PAYMENT_STATUS, String.format("결제 상태가 %s입니다. %s", status, s));
    }
}

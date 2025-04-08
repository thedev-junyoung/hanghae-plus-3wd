package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class UnsupportedPaymentMethodException extends BusinessException {
    public UnsupportedPaymentMethodException(String method) {
        super(ErrorCode.UNSUPPORTED_PAYMENT_METHOD, "지원되지 않는 결제 수단입니다: " + method);
    }
}

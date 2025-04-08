package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class PaymentNotFoundException extends BusinessException {
    public PaymentNotFoundException() {
        super(ErrorCode.PAYMENT_NOT_FOUND);
    }
    public PaymentNotFoundException(String pgTransactionId) {
        super(ErrorCode.PAYMENT_NOT_FOUND, "PG 거래 ID로 결제 정보를 찾을 수 없습니다: " + pgTransactionId);
    }
}

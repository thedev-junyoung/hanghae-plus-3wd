package kr.hhplus.be.server.domain.order.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class MinimumChargeAmountException extends BusinessException {
    public MinimumChargeAmountException() {
        super(ErrorCode.INVALID_AMOUNT); // 또는 새로운 ErrorCode 추가도 가능
    }
}
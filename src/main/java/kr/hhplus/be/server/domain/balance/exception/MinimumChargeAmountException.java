package kr.hhplus.be.server.domain.balance.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class MinimumChargeAmountException extends BusinessException {

    public MinimumChargeAmountException(long minimumChargeAmount) {
        super(
                ErrorCode.INVALID_AMOUNT,
                "최소 충전 금액은 " + minimumChargeAmount + "원 이상이어야 합니다."
        );
    }
}

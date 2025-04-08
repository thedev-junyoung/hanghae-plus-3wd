package kr.hhplus.be.server.domain.balance.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class NotEnoughBalanceException extends BusinessException {

    public NotEnoughBalanceException() {
        super(ErrorCode.INSUFFICIENT_BALANCE, "잔액이 부족합니다.");
    }

    public NotEnoughBalanceException(String message) {
        super(ErrorCode.INSUFFICIENT_BALANCE, message);
    }
}

package kr.hhplus.be.server.domain.balance.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class BalanceNotFoundException extends BusinessException {

    public BalanceNotFoundException(Long userId) {
        super(ErrorCode.BALANCE_NOT_FOUND, "해당 유저(" + userId + ")의 잔액 정보가 존재하지 않습니다.");
    }
}

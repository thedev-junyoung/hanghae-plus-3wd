package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class CouponAlreadyExhaustedException extends BusinessException {
    public CouponAlreadyExhaustedException() {
        super(ErrorCode.COUPON_EXHAUSTED);
    }
}

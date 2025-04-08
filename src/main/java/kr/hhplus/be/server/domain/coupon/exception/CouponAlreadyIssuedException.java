package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class CouponAlreadyIssuedException extends BusinessException {
    public CouponAlreadyIssuedException(Long userId, String code) {
        super(ErrorCode.COUPON_ALREADY_USED, "이미 발급받은 쿠폰입니다: userId=" + userId + ", code=" + code);
    }
}
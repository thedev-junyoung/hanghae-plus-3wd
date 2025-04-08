package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class CouponExpiredException extends BusinessException {
    public CouponExpiredException(String code) {
        super(ErrorCode.COUPON_EXPIRED, "쿠폰이 만료되었습니다: " + code);
    }
}

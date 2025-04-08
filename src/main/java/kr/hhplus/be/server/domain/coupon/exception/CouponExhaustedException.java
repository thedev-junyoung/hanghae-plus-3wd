package kr.hhplus.be.server.domain.coupon.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class CouponExhaustedException extends BusinessException {
    public CouponExhaustedException(String code) {
        super(ErrorCode.COUPON_EXHAUSTED, "쿠폰 수량이 모두 소진되었습니다: " + code);
    }
}
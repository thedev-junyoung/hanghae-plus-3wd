package kr.hhplus.be.server.domain.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponIssue {
    private final Long id;
    private final Long userId;
    private final Coupon coupon;
    private final LocalDateTime issuedAt;

    public static CouponIssue create(Long userId, Coupon coupon) {
        return new CouponIssue(null, userId, coupon, LocalDateTime.now());
    }
}

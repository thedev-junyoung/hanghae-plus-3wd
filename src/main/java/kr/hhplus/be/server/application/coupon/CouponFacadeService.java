package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.coupon.CouponIssueWriter;
import kr.hhplus.be.server.domain.coupon.CouponReader;
import kr.hhplus.be.server.domain.coupon.exception.CouponAlreadyIssuedException;
import kr.hhplus.be.server.domain.coupon.exception.CouponExhaustedException;
import kr.hhplus.be.server.domain.coupon.exception.CouponExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFacadeService implements CouponUseCase {

    private final CouponReader couponReader;
    private final CouponIssueWriter couponIssueWriter;

    @Override
    public CouponResult issueLimitedCoupon(IssueLimitedCouponCommand command) {
        Coupon coupon = couponReader.findByCode(command.couponCode());

        if (coupon.isExpired()) {
            throw new CouponExpiredException(command.couponCode());
        }

        if (coupon.isExhausted()) {
            throw new CouponExhaustedException(command.couponCode());
        }

        if (couponIssueWriter.hasIssued(command.userId(), coupon.getId())) {
            throw new CouponAlreadyIssuedException(command.userId(), command.couponCode());
        }

        CouponIssue issue = couponIssueWriter.save(command.userId(), coupon);

        return CouponResult.from(issue);
    }
}

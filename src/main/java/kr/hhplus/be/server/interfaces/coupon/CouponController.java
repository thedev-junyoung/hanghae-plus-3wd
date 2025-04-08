package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.coupon.CouponResult;
import kr.hhplus.be.server.application.coupon.CouponUseCase;
import kr.hhplus.be.server.application.coupon.IssueLimitedCouponCommand;
import kr.hhplus.be.server.common.dto.CustomApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController implements CouponAPI {

    private final CouponUseCase couponUseCase;

    @Override
    public ResponseEntity<CustomApiResponse<Response>> limitedIssueCoupon(@Valid @RequestBody Request request) {
        IssueLimitedCouponCommand command = request.toCommand();

        CouponResult result = couponUseCase.issueLimitedCoupon(command);

        return ResponseEntity.ok(CustomApiResponse.success(
                new Response(result.userCouponId(), result.userId(), result.couponType(),
                        result.discountRate(), result.issuedAt(), result.expiryDate())
        ));
    }

    // --- Nested DTO ---
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private Long userId;

        @NotBlank
        private String couponCode;

        public IssueLimitedCouponCommand toCommand() {
            return new IssueLimitedCouponCommand(userId, couponCode);
        }

    }

    @Getter
    public static class Response {
        private final Long userCouponId;
        private final Long userId;
        private final String couponType;
        private final Integer discountRate;
        private final LocalDateTime issuedAt;
        private final LocalDateTime expiryDate;

        public Response(Long userCouponId, Long userId, String couponType, Integer discountRate,
                        LocalDateTime issuedAt, LocalDateTime expiryDate) {
            this.userCouponId = userCouponId;
            this.userId = userId;
            this.couponType = couponType;
            this.discountRate = discountRate;
            this.issuedAt = issuedAt;
            this.expiryDate = expiryDate;
        }
    }
}

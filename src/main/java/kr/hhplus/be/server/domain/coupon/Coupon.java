package kr.hhplus.be.server.domain.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Coupon {
    private final Long id;
    private final String code;
    private final String type; // ex: PERCENTAGE, FIXED
    private final Integer discountRate;
    private final Integer totalQuantity;
    private final Integer remainingQuantity;
    private final LocalDateTime validFrom;
    private final LocalDateTime validUntil;

    public boolean isExpired() {
        return validUntil.isBefore(LocalDateTime.now());
    }

    public boolean isExhausted() {
        return remainingQuantity <= 0;
    }
}


package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING) // ← 중요!
    @Column(nullable = false)
    private CouponType type;

    @Column(nullable = false)
    private Integer discountRate;

    @Column(nullable = false)
    private Integer totalQuantity;

    @Column(nullable = false)
    private Integer remainingQuantity;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    public Coupon(String code, CouponType type, Integer discountRate, Integer totalQuantity,
                  Integer remainingQuantity, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.code = code;
        this.type = type;
        this.discountRate = discountRate;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = remainingQuantity;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public boolean isExpired() {
        return validUntil.isBefore(LocalDateTime.now());
    }

    public boolean isExhausted() {
        return remainingQuantity <= 0;
    }

    public void decreaseQuantity() {
        if (isExhausted()) {
            throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        }
        this.remainingQuantity -= 1;
    }
}



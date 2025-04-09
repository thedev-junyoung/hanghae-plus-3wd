package kr.hhplus.be.server.domain.coupon;

public enum CouponType {
    PERCENTAGE {
        @Override
        public int applyDiscount(int price, int discountValue) {
            return price * (100 - discountValue) / 100;
        }
    },
    FIXED {
        @Override
        public int applyDiscount(int price, int discountValue) {
            return Math.max(price - discountValue, 0);
        }
    };

    public abstract int applyDiscount(int price, int discountValue);
}

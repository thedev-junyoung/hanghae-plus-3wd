package kr.hhplus.be.server.application.product;

public record PopularProductCriteria(int days, int limit) {
    public static PopularProductCriteria defaultSetting() {
        return new PopularProductCriteria(3, 5);
    }
}

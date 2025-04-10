package kr.hhplus.be.server.application.product;

public record GetPopularProductsCommand(int days, int limit) {

    public static GetPopularProductsCommand defaultConfig() {
        return new GetPopularProductsCommand(3, 5); // 최근 3일, 최대 5개
    }
}

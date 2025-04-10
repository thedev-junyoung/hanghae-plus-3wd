package kr.hhplus.be.server.application.product;

public record ProductDetailResult(ProductInfo product) {
    public static ProductDetailResult from(ProductInfo info) {
        return new ProductDetailResult(info);
    }

    public static ProductDetailResult fromDomain(kr.hhplus.be.server.domain.product.Product product, int stock) {
        return from(ProductInfo.from(product, stock));
    }
}

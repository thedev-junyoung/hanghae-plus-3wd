package kr.hhplus.be.server.application.product;

import java.util.List;

public record ProductDetailResult(
        ProductResult product
) {
    public static ProductDetailResult from(kr.hhplus.be.server.domain.product.Product product) {
        return new ProductDetailResult(ProductListResult.from(List.of(product)).products().get(0));
    }
}

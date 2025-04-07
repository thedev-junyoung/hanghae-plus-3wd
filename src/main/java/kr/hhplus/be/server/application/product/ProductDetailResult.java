package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public record ProductDetailResult(
        ProductResult product
) {
    public static ProductDetailResult from(Product product) {
        return new ProductDetailResult(ProductResult.from(product));
    }
}

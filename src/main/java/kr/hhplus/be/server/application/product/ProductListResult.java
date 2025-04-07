package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public record ProductListResult(
        List<ProductResult> products
) {
    public static ProductListResult from(List<Product> productList) {
        return new ProductListResult(
                productList.stream()
                        .map(ProductResult::from)
                        .toList()
        );
    }
}

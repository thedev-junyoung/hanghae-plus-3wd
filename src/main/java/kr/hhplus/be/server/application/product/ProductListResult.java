package kr.hhplus.be.server.application.product;

import java.util.List;

public record ProductListResult(
        List<ProductResult> products
) {
    public static ProductListResult from(List<kr.hhplus.be.server.domain.product.Product> products) {
        return new ProductListResult(
                products.stream().map(p -> new ProductResult(
                        p.getId(), p.getName(), p.getPrice().value(), p.getStock(),
                        p.getReleaseDate(), p.getImageUrl(), p.getCreatedAt(), p.getUpdatedAt()
                )).toList()
        );
    }
}

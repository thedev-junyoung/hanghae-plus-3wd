package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;

import java.math.BigDecimal;

public record PopularProductResult(
        Long id,
        String name,
        BigDecimal price,
        int salesCount
) {
    public static PopularProductResult from(Product product) {
        return new PopularProductResult(
                product.getId(),
                product.getName(),
                product.getPrice().value(),
                0 // salesCount는 별도 로직에서 가져와야 함
        );
    }

}

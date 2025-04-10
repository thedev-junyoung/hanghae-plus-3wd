package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.PopularProductResult;
import kr.hhplus.be.server.application.product.ProductDetailResult;
import kr.hhplus.be.server.application.product.ProductListResult;
import kr.hhplus.be.server.application.product.ProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    @Getter
    @AllArgsConstructor
    public static class ProductListResponse {
        private final List<ProductSummary> products;

        public static ProductListResponse from(ProductListResult result) {
            return new ProductListResponse(
                    result.products().stream().map(ProductSummary::from).toList()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ProductDetailResponse {
        private final ProductSummary product;

        public static ProductDetailResponse from(ProductDetailResult result) {
            return new ProductDetailResponse(ProductSummary.from(result.product()));
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ProductSummary {
        private final Long id;
        private final String name;
        private final BigDecimal price;
        private final LocalDate releaseDate;
        private final String imageUrl;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static ProductSummary from(ProductResult result) {
            return new ProductSummary(
                    result.id(),
                    result.name(),
                    result.price(),
                    result.releaseDate(),
                    result.imageUrl(),
                    result.createdAt(),
                    result.updatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PopularProductResponse {
        private final Long id;
        private final String name;
        private final BigDecimal price;
        private final int salesCount;

        public static PopularProductResponse from(PopularProductResult result) {
            return new PopularProductResponse(
                    result.id(),
                    result.name(),
                    result.price(),
                    result.salesCount()
            );
        }
    }
}

package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductResult(
        Long id,
        String name,
        BigDecimal price,
        int stock,
        LocalDate releaseDate,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getPrice().value(),
                product.getStock(),
                product.getReleaseDate(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

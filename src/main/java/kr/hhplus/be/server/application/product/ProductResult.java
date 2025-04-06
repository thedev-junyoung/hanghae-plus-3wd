package kr.hhplus.be.server.application.product;

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
) {}

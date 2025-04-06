package kr.hhplus.be.server.infrastructure.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class ProductEntity {

    @Id
    private Long id;

    private String name;

    private String brand;

    private BigDecimal price;

    private int stock;

    private LocalDate releaseDate;

    private int salesCount;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    // --- Entity -> Domain ---
    public Product toDomain() {
        return new Product(
                id,
                name,
                brand,
                Money.wons(price),
                stock,
                releaseDate,
                imageUrl,
                description,
                createdAt,
                updatedAt
        );
    }

    // --- Domain -> Entity ---
    public static ProductEntity from(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.id = product.getId();
        entity.name = product.getName();
        entity.brand = product.getBrand();
        entity.price = product.getPrice().value();
        entity.stock = product.getStock();
        entity.releaseDate = product.getReleaseDate();
        entity.imageUrl = product.getImageUrl();
        entity.description = product.getDescription();
        entity.createdAt = product.getCreatedAt();
        entity.updatedAt = product.getUpdatedAt();
        return entity;
    }
}

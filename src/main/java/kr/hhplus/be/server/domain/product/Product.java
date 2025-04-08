package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Embedded
    private Money price;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private LocalDate releaseDate;

    private String imageUrl;

    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Product create(String name, String brand, Money price, int size, int stock,
                                 LocalDate releaseDate, String imageUrl, String description) {
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product();
        product.name = name;
        product.brand = brand;
        product.price = price;
        product.size = size;
        product.stock = stock;
        product.releaseDate = releaseDate;
        product.imageUrl = imageUrl;
        product.description = description;
        product.createdAt = now;
        product.updatedAt = now;
        return product;
    }

    public void decreaseStock(int quantity) {
        if (!isAvailable(quantity)) {
            throw new InsufficientStockException();
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable(int quantity) {
        return this.stock >= quantity;
    }
}

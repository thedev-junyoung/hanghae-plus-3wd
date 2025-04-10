package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.vo.Money;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private LocalDate releaseDate;

    private String imageUrl;

    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Product create(String name, String brand, Money price,
                                 LocalDate releaseDate, String imageUrl, String description) {
        LocalDateTime now = LocalDateTime.now();
        Product product = new Product();
        product.name = name;
        product.brand = brand;
        product.price = price.value();
        product.releaseDate = releaseDate;
        product.imageUrl = imageUrl;
        product.description = description;
        product.createdAt = now;
        product.updatedAt = now;
        return product;
    }
    public void updateDescription(String newDescription) {
        if (newDescription != null && newDescription.length() > 500) {
            throw new IllegalArgumentException("설명은 500자를 초과할 수 없습니다.");
        }
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }
    public boolean isReleased() {
        return !releaseDate.isAfter(LocalDate.now());
    }

    public boolean isAvailable(int totalStock) {
        return isReleased() && totalStock > 0;
    }
    public boolean isSameBrand(String brandName) {
        return this.brand.equalsIgnoreCase(brandName);
    }
}

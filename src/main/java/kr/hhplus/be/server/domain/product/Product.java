package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @EqualsAndHashCode.Include
    private final Long id;

    private final String name;
    private final String brand;
    private Money price;
    private int size;
    private int stock;
    private final LocalDate releaseDate;
    private final String imageUrl;
    private final String description;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product create(Long id, String name, String brand, Money price, int size, int stock,
                                 LocalDate releaseDate, String imageUrl, String description) {
        LocalDateTime now = LocalDateTime.now();
        return new Product(id, name, brand, price, size, stock, releaseDate, imageUrl, description, now, now);
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

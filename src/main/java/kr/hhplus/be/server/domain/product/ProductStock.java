package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductStock {

    @Id
    private Long id;
    private Long productId;
    private int size;
    private int stockQuantity;
    private LocalDateTime updatedAt;

    public ProductStock(Long productId, int size, int stockQuantity) {
        this.id = null;
        this.productId = productId;
        this.size = size;
        this.stockQuantity = stockQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public static ProductStock of(Long productId, int size, int stockQuantity) {
        return new ProductStock(productId, size, stockQuantity);
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void decreaseStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new InsufficientStockException();
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable(int quantity) {
        return stockQuantity >= quantity;
    }

}

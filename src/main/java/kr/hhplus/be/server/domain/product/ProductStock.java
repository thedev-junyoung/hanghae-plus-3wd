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
        if (!Policy.canDecrease(stockQuantity, quantity)) {
            throw new InsufficientStockException("재고가 부족합니다. 현재: " + stockQuantity + ", 요청: " + quantity);
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable(int quantity) {
        return Policy.canDecrease(stockQuantity, quantity);
    }

    static class Policy {

        // 재고는 0 이상이어야 한다
        public static boolean canDecrease(int currentStock, int requestQuantity) {
            return currentStock >= requestQuantity;
        }

        // 사이즈별 관리 - 외부에서 사이즈 파라미터로 관리됨 (도메인 설계로 분리되어 있음)
    }
}

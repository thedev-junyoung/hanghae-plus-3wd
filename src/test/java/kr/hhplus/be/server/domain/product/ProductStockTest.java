package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductStockTest {

    @Test
    @DisplayName("재고를 정상적으로 증가시킬 수 있다")
    void increaseStock_shouldAddQuantity() {
        ProductStock stock = ProductStock.of(1L, 270, 5);

        stock.increaseStock(3);

        assertThat(stock.getStockQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("재고를 정상적으로 차감할 수 있다")
    void decreaseStock_shouldSubtractQuantity() {
        ProductStock stock = ProductStock.of(1L, 270, 10);

        stock.decreaseStock(4);

        assertThat(stock.getStockQuantity()).isEqualTo(6);
    }

    @Test
    @DisplayName("재고가 부족하면 예외가 발생한다")
    void decreaseStock_shouldFail_whenInsufficient() {
        ProductStock stock = ProductStock.of(1L, 270, 2);

        assertThatThrownBy(() -> stock.decreaseStock(5))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("재고가 부족");
    }

    @Test
    @DisplayName("isAvailable은 요청 수량에 대해 재고가 충분한지 판단한다")
    void isAvailable_shouldReturnTrueOrFalseProperly() {
        ProductStock stock = ProductStock.of(1L, 270, 3);

        assertThat(stock.isAvailable(2)).isTrue();
        assertThat(stock.isAvailable(4)).isFalse();
    }
}

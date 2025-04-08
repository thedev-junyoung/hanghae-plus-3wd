package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.common.vo.Money;
import kr.hhplus.be.server.domain.product.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("재고 차감에 성공한다")
    void decreaseStock_success() {
        // given
        Product product = Product.create( "Nike", "Nike", Money.wons(100000),260, 10,
                LocalDate.now(), "/img.jpg", "Good shoes");

        // when
        product.decreaseStock(3);

        // then
        assertThat(product.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고 부족 시 예외가 발생한다")
    void decreaseStock_fail_insufficient() {
        // given
        Product product = Product.create( "Nike", "Nike", Money.wons(100000),260, 5,
                LocalDate.now(), "/img.jpg", "Limited");

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(10))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다")
    void increaseStock() {
        // given
        Product product = Product.create( "Nike", "Nike", Money.wons(100000),260, 3,
                LocalDate.now(), "/img.jpg", "Sample");

        // when
        product.increaseStock(2);

        // then
        assertThat(product.getStock()).isEqualTo(5);
    }

    @Test
    @DisplayName("재고 가용 여부를 확인한다")
    void isAvailable() {
        // given
        Product product = Product.create( "Nike", "Nike", Money.wons(100000),260, 5,
                LocalDate.now(), "/img.jpg", "Sample");

        // expect
        assertThat(product.isAvailable(3)).isTrue();
        assertThat(product.isAvailable(10)).isFalse();
    }
}

package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    private final Product product = Product.create(
            "NIKE AIR MAX",
            "NIKE",
            Money.wons(159000),
            LocalDate.now().minusDays(1), // 어제 출시
            "https://image.example.com/shoe.jpg",
            "기존 설명"
    );

    @Test
    @DisplayName("설명을 정상적으로 수정할 수 있다")
    void updateDescription_success() {
        // given
        String newDesc = "변경된 설명입니다.";

        // when
        product.updateDescription(newDesc);

        // then
        assertThat(product.getDescription()).isEqualTo(newDesc);
    }

    @Test
    @DisplayName("설명이 500자를 초과하면 예외가 발생한다")
    void updateDescription_fail_whenTooLong() {
        String longDesc = "a".repeat(501);

        assertThatThrownBy(() -> product.updateDescription(longDesc))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("500자를 초과");
    }

    @Test
    @DisplayName("출시일이 오늘 이전이면 출시된 상품으로 간주한다")
    void isReleased_shouldReturnTrue_whenReleasedDatePassed() {
        assertThat(product.isReleased()).isTrue();
    }

    @Test
    @DisplayName("재고가 있으면 구매 가능 상태로 판단한다")
    void isAvailable_shouldReturnTrue_whenStockExists() {
        assertThat(product.isAvailable(10)).isTrue();
    }

    @Test
    @DisplayName("재고가 0이면 구매 불가능 상태로 판단한다")
    void isAvailable_shouldReturnFalse_whenStockIsZero() {
        assertThat(product.isAvailable(0)).isFalse();
    }

    @Test
    @DisplayName("브랜드명이 일치하면 true를 반환한다 (대소문자 무시)")
    void isSameBrand_shouldReturnTrue_whenMatchingIgnoreCase() {
        assertThat(product.isSameBrand("nike")).isTrue();
        assertThat(product.isSameBrand("NIKE")).isTrue();
        assertThat(product.isSameBrand("adidas")).isFalse();
    }
}

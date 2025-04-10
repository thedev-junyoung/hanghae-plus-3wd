package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.product.exception.DescriptionTooLongException;
import kr.hhplus.be.server.domain.product.exception.ProductNotReleasedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    private final Product product = Product.create(
            "NIKE AIR MAX",
            "NIKE",
            Money.wons(159000),
            LocalDate.now().minusDays(1), // 출시일: 어제
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
        // given
        String longDesc = "a".repeat(501);

        // expect
        assertThatThrownBy(() -> product.updateDescription(longDesc))
                .isInstanceOf(DescriptionTooLongException.class)
                .hasMessageContaining("500자");
    }

    @Test
    @DisplayName("출시일이 오늘 이전이면 출시된 상품으로 간주한다")
    void isReleased_shouldReturnTrue_whenReleasedDatePassed() {
        assertThat(product.isReleased()).isTrue();
    }

    @Test
    @DisplayName("재고가 있고 출시되었으면 주문 가능하다")
    void validateOrderable_success() {
        assertThatCode(() -> product.validateOrderable(10)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("출시되지 않은 상품은 주문할 수 없다")
    void validateOrderable_fail_whenNotReleased() {
        Product notReleased = Product.create(
                "NEW SHOE",
                "NIKE",
                Money.wons(100000),
                LocalDate.now().plusDays(1), // 내일 출시
                null,
                "desc"
        );

        assertThatThrownBy(() -> notReleased.validateOrderable(5))
                .isInstanceOf(ProductNotReleasedException.class)
                .hasMessageContaining("출시되지 않은");
    }

    @Test
    @DisplayName("재고가 없으면 주문할 수 없다")
    void validateOrderable_fail_whenStockIsZero() {
        assertThatThrownBy(() -> product.validateOrderable(0))
                .isInstanceOf(ProductOutOfStockException.class)
                .hasMessageContaining("재고가 부족");
    }

    @Test
    @DisplayName("브랜드명이 일치하면 true를 반환한다 (대소문자 무시)")
    void isSameBrand_shouldReturnTrue_whenMatchingIgnoreCase() {
        assertThat(product.isSameBrand("nike")).isTrue();
        assertThat(product.isSameBrand("NIKE")).isTrue();
        assertThat(product.isSameBrand("adidas")).isFalse();
    }
}

package kr.hhplus.be.server.domain.productstatistics;

import kr.hhplus.be.server.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProductStatisticsTest {

    @Test
    @DisplayName("신규 통계 객체 생성 시 초기값이 0이다")
    void create_initialState() {
        ProductStatistics stats = ProductStatistics.create(1L, LocalDate.of(2025, 4, 8));

        assertThat(stats.getProductId()).isEqualTo(1L);
        assertThat(stats.getStatDate()).isEqualTo(LocalDate.of(2025, 4, 8));
        assertThat(stats.getSalesCount()).isZero();
        assertThat(stats.getSalesAmount()).isEqualTo(Money.wons(0));
    }

    @Test
    @DisplayName("판매 내역 추가 시 수량과 금액이 누적된다")
    void addSales_shouldAccumulateCountAndAmount() {
        ProductStatistics stats = ProductStatistics.create(1L, LocalDate.now());

        stats.addSales(2, Money.wons(5000)); // 1차 추가
        stats.addSales(3, Money.wons(4000)); // 2차 추가

        assertThat(stats.getSalesCount()).isEqualTo(5); // 2 + 3
        assertThat(stats.getSalesAmount()).isEqualTo(Money.wons(5000 * 2 + 4000 * 3));
    }

    @Test
    @DisplayName("동일 ID와 날짜는 같은 통계 객체로 간주된다 (equals/hashCode)")
    void equalsAndHashCode_shouldCompareByProductIdAndDate() {
        LocalDate today = LocalDate.now();

        ProductStatistics a = ProductStatistics.create(1L, today);
        ProductStatistics b = ProductStatistics.create(1L, today);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    @DisplayName("날짜가 다르면 equals 비교 시 다른 객체로 간주된다")
    void notEquals_whenStatDateDifferent() {
        ProductStatistics a = ProductStatistics.create(1L, LocalDate.of(2025, 4, 7));
        ProductStatistics b = ProductStatistics.create(1L, LocalDate.of(2025, 4, 8));

        assertThat(a).isNotEqualTo(b);
    }
}

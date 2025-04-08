package kr.hhplus.be.server.domain.productstatistics;

import kr.hhplus.be.server.common.vo.Money;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductStatistics {

    @EqualsAndHashCode.Include
    private final Long productId;


    @EqualsAndHashCode.Include
    private final LocalDate statDate;

    private int salesCount;

    private Money salesAmount;

    public static ProductStatistics create(Long productId, LocalDate date) {
        return new ProductStatistics(productId, date, 0, Money.wons(0));
    }

    public void addSales(int quantity, Money unitPrice) {
        this.salesCount += quantity;
        this.salesAmount = this.salesAmount.add(unitPrice.multiply(quantity));
    }
}

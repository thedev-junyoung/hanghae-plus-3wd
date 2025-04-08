package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.vo.Money;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class OrderItem {
    private final Long productId;
    private final int quantity;
    private final int size;
    private final Money price;

    public Money calculateTotal() {
        return price.multiply(quantity);
    }
}

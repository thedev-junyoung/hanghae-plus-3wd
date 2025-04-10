package kr.hhplus.be.server.domain.balance;

import kr.hhplus.be.server.domain.balance.exception.NotEnoughBalanceException;
import kr.hhplus.be.server.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class BalanceTest {

    @Test
    @DisplayName("잔액을 충전할 수 있다")
    void chargeBalance() {
        Balance balance = Balance.createNew(1L, 100L, Money.wons(1000));
        balance.charge(Money.wons(500));

        assertThat(balance.getAmount()).isEqualTo(BigDecimal.valueOf(1500));
    }

    @Test
    @DisplayName("잔액을 차감할 수 있다")
    void decreaseBalance() {
        Balance balance = Balance.createNew(1L, 100L, Money.wons(1000));
        balance.decrease(Money.wons(300));

        assertThat(balance.getAmount()).isEqualTo(BigDecimal.valueOf(700));
    }

    @Test
    @DisplayName("잔액을 차감할 때 잔액이 부족하면 예외가 발생한다")
    void throwExceptionWhenBalanceIsNotEnough() {
        Balance balance = Balance.createNew(1L, 100L, Money.wons(500));

        assertThatThrownBy(() -> balance.decrease(Money.wons(600)))
                .isInstanceOf(NotEnoughBalanceException.class);
    }

    @Test
    @DisplayName("잔액이 충분한지 확인할 수 있다")
    void checkIfBalanceIsEnough() {
        Balance balance = Balance.createNew(1L, 100L, Money.wons(1000));

        assertThat(balance.hasEnough(Money.wons(1000))).isTrue();
        assertThat(balance.hasEnough(Money.wons(1500))).isFalse();
    }
}

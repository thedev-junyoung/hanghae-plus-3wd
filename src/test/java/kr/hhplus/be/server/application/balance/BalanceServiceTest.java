package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.exception.NotEnoughBalanceException;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.common.vo.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    BalanceService balanceService;

    @Test
    @DisplayName("잔액을 충전할 수 있다")
    void charge_success() {
        // given
        Balance existing = Balance.createNew(1L, 100L, Money.wons(1000));
        when(balanceRepository.findByUserId(100L)).thenReturn(Optional.of(existing));
        when(balanceRepository.save(eq(existing))).thenReturn(existing); // eq로 명시적 비교

        ChargeBalanceCommand command = new ChargeBalanceCommand(100L, BigDecimal.valueOf(500));

        // when
        BalanceInfo info = balanceService.charge(command);

        // then
        assertThat(info.amount()).isEqualTo(BigDecimal.valueOf(1500));
        verify(balanceRepository).save(existing);
    }

    @Test
    @DisplayName("잔액을 차감할 수 있다")
    void decrease_success() {
        // given
        Balance existing = Balance.createNew(1L, 100L, Money.wons(1000));
        when(balanceRepository.findByUserId(100L)).thenReturn(Optional.of(existing));
        when(balanceRepository.save(eq(existing))).thenReturn(existing);

        DecreaseBalanceCommand command = new DecreaseBalanceCommand(100L, BigDecimal.valueOf(500));

        // when
        boolean result = balanceService.decreaseBalance(command);

        // then
        assertThat(result).isTrue();

        ArgumentCaptor<Balance> captor = ArgumentCaptor.forClass(Balance.class);
        verify(balanceRepository).save(captor.capture());

        Balance saved = captor.getValue();
        assertThat(saved.getAmount()).isEqualTo(BigDecimal.valueOf(500)); // 1000 - 500 = 500
    }

    @Test
    @DisplayName("잔액이 부족하면 예외가 발생한다")
    void decrease_fail_not_enough_balance() {
        // givena
        Balance existing = Balance.createNew(1L, 100L, Money.wons(300));
        when(balanceRepository.findByUserId(100L)).thenReturn(Optional.of(existing));

        DecreaseBalanceCommand command = new DecreaseBalanceCommand(100L, BigDecimal.valueOf(500));

        // when & then
        assertThatThrownBy(() -> balanceService.decreaseBalance(command))
                .isInstanceOf(NotEnoughBalanceException.class);

        verify(balanceRepository, never()).save(any());
    }
}
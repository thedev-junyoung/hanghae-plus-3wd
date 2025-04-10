package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.balance.exception.BalanceNotFoundException;
import kr.hhplus.be.server.common.vo.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceService implements BalanceUseCase {


    private final BalanceRepository balanceRepository;

    @Override
    public BalanceResult charge(ChargeBalanceCommand command) {
        Balance balance = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new BalanceNotFoundException(command.userId()));

        balance.charge(Money.wons(command.amount()));

        Balance updated = balanceRepository.save(balance);
        return BalanceResult.from(balance);
    }

    @Override
    public BalanceResult getBalance(Long userId) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new BalanceNotFoundException(userId));

        return BalanceResult.from(balance);
    }

    @Override
    public boolean decreaseBalance(DecreaseBalanceCommand command) {
        Balance balance = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new BalanceNotFoundException(command.userId()));

        balance.decrease(Money.wons(command.amount()));

        balanceRepository.save(balance);

        return true;
    }
}

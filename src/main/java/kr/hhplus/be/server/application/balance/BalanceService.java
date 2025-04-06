package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.BalanceNotFoundException;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.infrastructure.balance.BalanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceService implements BalanceUseCase {


    private final BalanceRepository balanceRepository;

    @Override
    public BalanceResult charge(ChargeBalanceCommand command) {
        BalanceEntity entity = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new BalanceNotFoundException(command.userId()));

        Balance domain = entity.toDomain();
        domain.charge(Money.wons(command.amount()));

        BalanceEntity updated = balanceRepository.save(BalanceEntity.from(domain));
        return BalanceResult.from(updated.toDomain());
    }

    @Override
    public BalanceResult getBalance(Long userId) {
        BalanceEntity entity = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new BalanceNotFoundException(userId));

        return BalanceResult.from(entity.toDomain());
    }

    @Override
    public boolean decreaseBalance(DecreaseBalanceCommand command) {
        BalanceEntity entity = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new BalanceNotFoundException(command.userId()));

        Balance domain = entity.toDomain();
        domain.decrease(Money.wons(command.amount()));

        balanceRepository.save(BalanceEntity.from(domain));

        return true;
    }
}

package kr.hhplus.be.server.application.balance;

import kr.hhplus.be.server.domain.balance.BalanceChangeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BalanceFacade {

    private final BalanceUseCase balanceUseCase;
    private final BalanceHistoryUseCase historyUseCase;

    public BalanceResult chargeAndRecord(ChargeBalanceCriteria criteria) {
        BalanceInfo info = balanceUseCase.charge(
                new ChargeBalanceCommand(criteria.userId(), criteria.amount())
        );

        historyUseCase.recordHistory(
                new RecordBalanceHistoryCommand(
                        criteria.userId(),
                        criteria.amount(),
                        BalanceChangeType.CHARGE,
                        criteria.reason()
                )
        );

        return BalanceResult.fromInfo(info);
    }


}


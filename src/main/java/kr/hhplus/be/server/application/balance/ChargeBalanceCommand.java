package kr.hhplus.be.server.application.balance;


public record ChargeBalanceCommand(
        Long userId,
        long amount
) {
    public static ChargeBalanceCommand from(ChargeBalanceCriteria criteria) {
        return new ChargeBalanceCommand(criteria.userId(), criteria.amount());
    }
}

package kr.hhplus.be.server.domain.balance.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("잔액이 부족합니다.");
    }

    public NotEnoughBalanceException(String message) {
        super(message);
    }
}

package kr.hhplus.be.server.domain;

public class BalanceNotFoundException extends RuntimeException {
    public BalanceNotFoundException(Long userId) {
        super("해당 유저(" + userId + ")의 잔액 정보가 존재하지 않습니다.");
    }
}

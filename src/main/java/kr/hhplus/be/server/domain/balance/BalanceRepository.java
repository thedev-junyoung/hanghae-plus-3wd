package kr.hhplus.be.server.domain.balance;

import kr.hhplus.be.server.infrastructure.balance.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository{
    Balance save(Balance balance);
    Optional<Balance> findByUserId(Long userId);
}

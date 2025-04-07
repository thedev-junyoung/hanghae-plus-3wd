package kr.hhplus.be.server.domain.payment;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findByPgTransactionId(String pgTransactionId);
}

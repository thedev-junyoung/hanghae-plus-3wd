package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.common.vo.Money;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.domain.payment.exception.UnsupportedPaymentMethodException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BalancePaymentProcessor balancePaymentProcessor;
    private final PaymentRepository paymentRepository;

    public Payment initiate(String orderId, Money amount, String method) {
        Payment payment = Payment.initiate(orderId, amount, method);
        paymentRepository.save(payment);
        return payment;
    }

    public void markSuccess(Payment payment) {
        payment.complete();
        paymentRepository.save(payment);
    }

    public void markFailure(Payment payment) {
        payment.fail();
        paymentRepository.save(payment);
    }

    public boolean process(RequestPaymentCommand command, Order order, Payment payment) {
        PaymentProcessor processor = resolveProcessor(command.method());
        return processor.process(command, order, payment);
    }

    private PaymentProcessor resolveProcessor(String method) {
        if ("BALANCE".equalsIgnoreCase(method)) {
            return balancePaymentProcessor;
        }
        throw new UnsupportedPaymentMethodException(method);
    }

    public Payment getByPgTraansactionId(String pgTransactionId) {
        return paymentRepository.findByPgTransactionId(pgTransactionId)
                .map(payment -> Payment.initiate(
                        payment.getOrderId(),
                        Money.wons(payment.getAmount().value()),
                        payment.getMethod()
                ))
                .orElseThrow(() -> new PaymentNotFoundException(pgTransactionId));
    }
}


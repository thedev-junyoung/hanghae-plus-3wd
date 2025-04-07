package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;

public interface PaymentProcessor {
    void process(RequestPaymentCommand command, Order order, Payment payment);
}


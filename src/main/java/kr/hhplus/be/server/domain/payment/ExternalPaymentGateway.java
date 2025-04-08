package kr.hhplus.be.server.domain.payment;

public interface ExternalPaymentGateway {
    boolean requestPayment(String orderId);
    boolean confirmPayment(String transactionId);
}

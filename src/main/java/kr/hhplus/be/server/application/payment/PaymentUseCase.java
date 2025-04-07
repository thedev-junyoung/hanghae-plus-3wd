package kr.hhplus.be.server.application.payment;

public interface PaymentUseCase {
    /**
     * 결제 요청을 처리
     */
    PaymentResult requestPayment(RequestPaymentCommand command);

    /**
     * 결제 확인을 처리
     */
    PaymentResult confirmPayment(ConfirmPaymentCommand command);
}

package kr.hhplus.be.server.infrastructure.external;

import kr.hhplus.be.server.domain.payment.ExternalPaymentGateway;
import org.springframework.stereotype.Component;

@Component
public class ExternalPaymentGatewayClient implements ExternalPaymentGateway {

    @Override
    public boolean requestPayment(String orderId) {
        return true; // 실제로는 외부 API 호출
    }

    @Override
    public boolean confirmPayment(String transactionId) {
        return true; // 실제로는 외부 API 호출
    }
}

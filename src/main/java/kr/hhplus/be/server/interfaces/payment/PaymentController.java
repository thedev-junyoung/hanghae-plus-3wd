package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.*;
import kr.hhplus.be.server.common.dto.CustomApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentAPI {

    private final PaymentFacadeService paymentFacadeService;

    @Override
    public ResponseEntity<CustomApiResponse<Response.PaymentResponse>> requestPayment(Request.RequestPayment request) {
        RequestPaymentCommand command = request.toCommand();
        PaymentResult result = paymentFacadeService.requestPayment(command);
        return ResponseEntity.ok(CustomApiResponse.success(Response.PaymentResponse.from(result)));
    }

    @Override
    public ResponseEntity<CustomApiResponse<Response.PaymentResponse>> confirmPayment(String pgTransactionId) {
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand(pgTransactionId));
        return ResponseEntity.ok(CustomApiResponse.success(Response.PaymentResponse.from(result)));
    }

    // ===== NESTED DTO =====
    public static class Request {
        @Getter
        @AllArgsConstructor
        public static class RequestPayment {
            private String orderId;
            private Long userId;
            private String method;

            public RequestPaymentCommand toCommand() {
                return new RequestPaymentCommand(orderId, userId, method);
            }
        }
    }

    public static class Response {
        @Getter
        @AllArgsConstructor
        public static class PaymentResponse {
            private String paymentId;
            private String orderId;
            private BigDecimal amount;
            private String method;
            private String status;
            private LocalDateTime createdAt;

            public static PaymentResponse from(PaymentResult result) {
                return new PaymentResponse(
                        result.paymentId(),
                        result.orderId(),
                        result.amount(),
                        result.method(),
                        result.status().name(),
                        result.createdAt()
                );
            }
        }
    }
}

package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.*;
import kr.hhplus.be.server.common.dto.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PaymentController implements PaymentAPI {

    private final PaymentFacadeService paymentFacadeService;

    @Override
    public ResponseEntity<CustomApiResponse<PaymentResponse>> requestPayment(PaymentRequest request) {
        RequestPaymentCommand command = request.toCommand();
        PaymentResult result = paymentFacadeService.requestPayment(command);
        return ResponseEntity.ok(CustomApiResponse.success(PaymentResponse.from(result)));
    }


    @Override
    public ResponseEntity<CustomApiResponse<PaymentResponse>> confirmPayment(String pgTransactionId) {
        PaymentResult result = paymentFacadeService.confirmPayment(new ConfirmPaymentCommand(pgTransactionId));
        return ResponseEntity.ok(CustomApiResponse.success(PaymentResponse.from(result)));
    }
}

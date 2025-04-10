package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.hhplus.be.server.application.balance.BalanceUseCase;
import kr.hhplus.be.server.application.balance.ChargeBalanceCommand;
import kr.hhplus.be.server.application.balance.BalanceResult;
import kr.hhplus.be.server.common.dto.CustomApiResponse;

@RestController
@RequestMapping("/api/v1/balances")
@RequiredArgsConstructor
@Tag(name = "Balance", description = "잔액 관리 API")
public class BalanceController implements BalanceAPI{

    private final BalanceUseCase balanceUseCase;

    @PostMapping("/charge")
    @Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전합니다.")
    public ResponseEntity<CustomApiResponse<BalanceResponse>> charge(@Valid @RequestBody BalanceRequest request) {
        ChargeBalanceCommand command = new ChargeBalanceCommand(
                request.getUserId(),
                request.getAmount()
        );

        BalanceResult result = balanceUseCase.charge(command);

        return ResponseEntity.ok(CustomApiResponse.success(
                new BalanceResponse(result.userId(), result.balance(), result.updatedAt())
        ));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회합니다.")
    public ResponseEntity<CustomApiResponse<BalanceResponse>> getBalance(@PathVariable Long userId) {
        BalanceResult result = balanceUseCase.getBalance(userId);
        return ResponseEntity.ok(CustomApiResponse.success(
                new BalanceResponse(result.userId(), result.balance(), result.updatedAt())
        ));
    }
}
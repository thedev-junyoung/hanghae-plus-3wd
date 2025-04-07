package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<CustomApiResponse<Response>> charge(@Valid @RequestBody Request request) {
        // 커맨드 객체 생성 시 BigDecimal 그대로 전달
        ChargeBalanceCommand command = new ChargeBalanceCommand(
                request.getUserId(),
                request.getAmount()
        );

        BalanceResult result = balanceUseCase.charge(command);

        return ResponseEntity.ok(CustomApiResponse.success(
                new Response(result.userId(), result.balance(), result.updatedAt())
        ));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "잔액 조회", description = "사용자의 현재 잔액을 조회합니다.")
    public ResponseEntity<CustomApiResponse<Response>> getBalance(@PathVariable Long userId) {
        BalanceResult result = balanceUseCase.getBalance(userId);
        return ResponseEntity.ok(CustomApiResponse.success(
                new Response(result.userId(), result.balance(), result.updatedAt())
        ));
    }

    // --- nested static DTO classes ---
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull(message = "사용자 ID는 필수입니다.")
        private Long userId;

        @NotNull(message = "충전 금액은 필수입니다.")
        @Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다.")
        private BigDecimal amount;
    }

    @Getter
    public static class Response {
        @Schema(description = "사용자 ID", example = "12345")
        private final Long userId;

        @Schema(description = "현재 잔액", example = "150000")
        private final BigDecimal balance;

        @Schema(description = "잔액 갱신 시각", example = "2025-04-02T10:30:00")
        private final LocalDateTime updatedAt;

        public Response(Long userId, BigDecimal balance, LocalDateTime updatedAt) {
            this.userId = userId;
            this.balance = balance;
            this.updatedAt = updatedAt;
        }
    }
}
package kr.hhplus.be.server.interfaces.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BalanceRequest {

    @NotNull
    @Schema(description = "사용자 ID", example = "12345")
    private Long userId;

    @NotNull
    @Min(0)
    @Schema(description = "충전 금액", example = "50000")
    private BigDecimal amount;
}

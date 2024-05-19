package com.gl.financemanager.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class LoanDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Integer id;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "1")
  @DecimalMax(value = "1e9")
  private BigDecimal amount;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @Size(min = 1, max = 30)
  @NotNull
  private String name;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "0.01")
  @DecimalMax(value = "1000")
  private BigDecimal interestRate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "1")
  private BigDecimal monthlyRepayment;

  @AssertTrue
  @SuppressWarnings("unused")
  private boolean isMonthlyRepaymentMaxOk() {
    return monthlyRepayment.compareTo(amount) <= 0;
  }
}

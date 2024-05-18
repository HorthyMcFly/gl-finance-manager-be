package com.gl.financemanager.asset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetInvestmentBalanceDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "1")
  @DecimalMax(value = "1e9")
  private BigDecimal amount;

}

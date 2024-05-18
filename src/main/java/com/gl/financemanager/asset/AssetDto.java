package com.gl.financemanager.asset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AssetDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Integer id;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "1")
  @DecimalMax(value = "1e9")
  private BigDecimal amount;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @Size(min = 1, max = 30)
  private String name;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Boolean useInvestmentBalance;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  private AssetType assetType;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private LocalDate maturityDate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @NotNull
  @DecimalMin(value = "0.01")
  @DecimalMax(value = "1000")
  private BigDecimal interestRate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Integer interestPaymentMonth;

}

package com.gl.financemanager.income;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class IncomeDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Integer id;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @DecimalMin(value = "1")
  @DecimalMax(value = "1e9")
  private BigDecimal amount;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @Size(min = 1, max = 30)
  private String source;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  @Size(max = 100)
  private String comment;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean editable;
}

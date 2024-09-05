package com.gl.financemanager.expense.dto;

import com.gl.financemanager.expense.entity.ExpenseCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ExpenseCategoryLimitDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = true)
  private Integer id;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private ExpenseCategory expenseCategory;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  @DecimalMin(value = "1")
  @DecimalMax(value = "1e17")
  private BigDecimal expenseLimit;
}

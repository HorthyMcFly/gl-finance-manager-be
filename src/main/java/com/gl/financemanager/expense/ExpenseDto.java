package com.gl.financemanager.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class ExpenseDto {

  private Integer id;

  private Integer periodId;

  private Integer loanId;

  private BigDecimal amount;

  private String recipient;

  private ExpenseCategory expenseCategory;

  private String comment;
}

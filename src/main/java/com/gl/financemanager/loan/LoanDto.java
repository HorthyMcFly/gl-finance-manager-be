package com.gl.financemanager.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class LoanDto {

  private Integer id;

  private BigDecimal amount;

  private String name;

  private BigDecimal interestRate;

  private BigDecimal monthlyRepayment;
}

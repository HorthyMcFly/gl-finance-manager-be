package com.gl.financemanager.income;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class IncomeDto {

  public Integer id;

  private BigDecimal amount;

  private String source;

  private String comment;
}

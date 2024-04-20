package com.gl.financemanager.balance;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class BalanceDto {

  private Integer id;

  @NotNull
  private BigDecimal balance;

  @NotNull
  private BigDecimal investmentBalance;
}

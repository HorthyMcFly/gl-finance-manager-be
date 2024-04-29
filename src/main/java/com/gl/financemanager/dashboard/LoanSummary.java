package com.gl.financemanager.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoanSummary {

  private BigDecimal totalLoanValue;

}

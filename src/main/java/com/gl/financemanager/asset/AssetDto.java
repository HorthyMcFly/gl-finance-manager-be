package com.gl.financemanager.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class AssetDto {

  private Integer id;

  private BigDecimal amount;

  private String name;

  private AssetType assetType;

  private LocalDate maturityDate;

  private BigDecimal interestRate;

  private LocalDate interestPaymentDate;

}

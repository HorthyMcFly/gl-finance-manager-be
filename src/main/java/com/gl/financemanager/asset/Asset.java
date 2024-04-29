package com.gl.financemanager.asset;

import com.gl.financemanager.auth.FmUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ASSET")
public class Asset {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  private BigDecimal amount;

  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ASSET_TYPE")
  private AssetType assetType;

  @Column(name = "MATURITY_DATE")
  private LocalDate maturityDate;

  @Column(name = "INTEREST_RATE")
  private BigDecimal interestRate;

  @Column(name = "INTEREST_PAYMENT_DATE")
  private LocalDate interestPaymentDate;

}

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
  @JoinColumn(name = "FM_USER", nullable = false)
  private FmUser fmUser;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "ASSET_TYPE", nullable = false)
  private AssetType assetType;

  @Column(name = "MATURITY_DATE", nullable = false)
  private LocalDate maturityDate;

  @Column(name = "INTEREST_RATE", nullable = false)
  private BigDecimal interestRate;

  @Column(name = "INTEREST_PAYMENT_MONTH")
  private Integer interestPaymentMonth;

}

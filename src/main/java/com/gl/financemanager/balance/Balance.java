package com.gl.financemanager.balance;

import com.gl.financemanager.auth.FmUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BALANCE")
public class Balance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  private BigDecimal balance;

  @Column(name = "INVESTMENT_BALANCE")
  private BigDecimal investmentBalance;
}

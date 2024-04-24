package com.gl.financemanager.loan;

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
@Table(name = "LOAN")
public class Loan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  private BigDecimal amount;

  private String name;

  @Column(name = "INTEREST_RATE")
  private BigDecimal interestRate;

  @Column(name = "MONTHLY_REPAYMENT")
  private BigDecimal monthlyRepayment;
}

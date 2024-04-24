package com.gl.financemanager.expense;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.loan.Loan;
import com.gl.financemanager.period.FmPeriod;
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
@Table(name = "EXPENSE")
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_PERIOD")
  private FmPeriod fmPeriod;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LOAN")
  private Loan loan;

  private BigDecimal amount;

  private String recipient;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "EXPENSE_CATEGORY")
  private ExpenseCategory expenseCategory;

  private String comment;
}

package com.gl.financemanager.expense.entity;

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
@Table(name = "EXPENSE_CATEGORY_LIMIT")
public class ExpenseCategoryLimit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  @OneToOne
  @JoinColumn(name = "EXPENSE_CATEGORY")
  private ExpenseCategory expenseCategory;

  @Column(name = "EXPENSE_LIMIT")
  private BigDecimal expenseLimit;

}

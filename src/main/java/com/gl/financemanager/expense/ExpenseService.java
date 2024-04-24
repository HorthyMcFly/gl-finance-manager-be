package com.gl.financemanager.expense;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {

  private ExpenseRepository expenseRepository;
  private ExpenseCategoryRepository expenseCategoryRepository;

  public List<ExpenseDto> getExpensesForLoggedInUserByPeriodId(Integer periodId) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return expenseRepository.findByFmUserUsernameAndFmPeriodId(loggedInUsername, periodId)
        .stream().map(ExpenseService::toDto).toList();
  }

  public List<ExpenseCategory> getExpenseCategories() {
    return this.expenseCategoryRepository.findAll();
  }

  static ExpenseDto toDto(Expense expense) {
    var loan = expense.getLoan();
    return ExpenseDto.builder()
        .id(expense.getId())
        .periodId(expense.getFmPeriod().getId())
        .loanId(loan != null ? loan.getId() : null)
        .amount(expense.getAmount())
        .recipient(expense.getRecipient())
        .expenseCategory(expense.getExpenseCategory())
        .comment(expense.getComment())
        .build();
  }
}

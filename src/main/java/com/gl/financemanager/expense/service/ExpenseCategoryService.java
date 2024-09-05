package com.gl.financemanager.expense.service;

import com.gl.financemanager.expense.dto.ExpenseCategoryLimitDto;
import com.gl.financemanager.expense.entity.ExpenseCategory;
import com.gl.financemanager.expense.entity.ExpenseCategoryLimit;
import com.gl.financemanager.expense.repository.ExpenseCategoryLimitRepository;
import com.gl.financemanager.expense.repository.ExpenseCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseCategoryService {

  private final ExpenseCategoryRepository expenseCategoryRepository;
  private ExpenseCategoryLimitRepository expenseCategoryLimitRepository;

  public List<ExpenseCategory> getExpenseCategories() {
    return this.expenseCategoryRepository.findAll();
  }

  public List<ExpenseCategoryLimitDto> findAllExpenseCategoryLimitsForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return expenseCategoryLimitRepository.findAllByFmUserUsername(loggedInUsername).stream()
        .map(ExpenseCategoryService::expenseCategoryLimitToDto).toList();
  }

  static ExpenseCategoryLimitDto expenseCategoryLimitToDto(ExpenseCategoryLimit expenseCategoryLimit) {
    return ExpenseCategoryLimitDto.builder()
        .id(expenseCategoryLimit.getId())
        .expenseCategory(expenseCategoryLimit.getExpenseCategory())
        .expenseLimit(expenseCategoryLimit.getExpenseLimit())
        .build();
  }
}

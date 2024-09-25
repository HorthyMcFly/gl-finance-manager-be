package com.gl.financemanager.expense.service;

import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.expense.dto.ExpenseCategoryLimitDto;
import com.gl.financemanager.expense.entity.ExpenseCategory;
import com.gl.financemanager.expense.entity.ExpenseCategoryLimit;
import com.gl.financemanager.expense.repository.ExpenseCategoryLimitRepository;
import com.gl.financemanager.expense.repository.ExpenseCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseCategoryService {

  private final ExpenseCategoryRepository expenseCategoryRepository;
  private ExpenseCategoryLimitRepository expenseCategoryLimitRepository;
  private final UserRepository userRepository;

  public List<ExpenseCategory> getExpenseCategories() {
    return this.expenseCategoryRepository.findAll();
  }

  public List<ExpenseCategoryLimitDto> findAllExpenseCategoryLimitsForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return expenseCategoryLimitRepository.findAllByFmUserUsername(loggedInUsername).stream()
        .map(ExpenseCategoryService::expenseCategoryLimitToDto).toList();
  }

  @Transactional
  public ExpenseCategoryLimitDto createExpenseCategoryLimit(ExpenseCategoryLimitDto expenseCategoryLimitDto) {
    if (expenseCategoryLimitDto.getId() != null) {
      throw new RuntimeException();
    }
    var loggedInUser = this.userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    if (loggedInUser.isEmpty()) {
      throw new RuntimeException();
    }
    var existingLimitForCategory = expenseCategoryLimitRepository
        .findByExpenseCategoryIdAndFmUserId(expenseCategoryLimitDto.getExpenseCategory().getId(),
            loggedInUser.get().getId());
    if (existingLimitForCategory.isPresent()) {
      throw new RuntimeException();
    }
    var newExpenseCategoryLimit = ExpenseCategoryService.fromExpenseCategoryLimitDto(expenseCategoryLimitDto);
    newExpenseCategoryLimit.setFmUser(loggedInUser.get());

    var expenseCategoryOpt = expenseCategoryRepository.findById(expenseCategoryLimitDto.getExpenseCategory().getId());
    assert(expenseCategoryOpt.isPresent());
    newExpenseCategoryLimit.setExpenseCategory(expenseCategoryOpt.get());
    var createdExpenseCategoryLimit = expenseCategoryLimitRepository.save(newExpenseCategoryLimit);
    return ExpenseCategoryService.expenseCategoryLimitToDto(createdExpenseCategoryLimit);
  }

  @Transactional
  public ExpenseCategoryLimitDto modifyExpenseCategoryLimit(ExpenseCategoryLimitDto expenseCategoryLimitDto) {
    var existingExpenseCategoryLimit = findExistingExpenseCategoryLimitIfValidId(expenseCategoryLimitDto.getId());
    existingExpenseCategoryLimit.setExpenseLimit(expenseCategoryLimitDto.getExpenseLimit());

    var modifiedExpenseLimitCategory = expenseCategoryLimitRepository.save(existingExpenseCategoryLimit);
    return ExpenseCategoryService.expenseCategoryLimitToDto(modifiedExpenseLimitCategory);
  }

  @Transactional
  public void deleteExpenseCategoryLimit(Integer id) {
    var existingExpenseCategoryLimit = findExistingExpenseCategoryLimitIfValidId(id);
    expenseCategoryLimitRepository.delete(existingExpenseCategoryLimit);
  }

  private ExpenseCategoryLimit findExistingExpenseCategoryLimitIfValidId(Integer id) {
    if (id == null) {
      throw new RuntimeException();
    }
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var existingExpenseCategoryOpt = expenseCategoryLimitRepository.findById(id);
    if (existingExpenseCategoryOpt.isEmpty()) {
      throw new RuntimeException();
    }
    var existingExpenseCategoryLimit = existingExpenseCategoryOpt.get();
    if (!existingExpenseCategoryLimit.getFmUser().getUsername().equals(loggedInUsername)) {
      throw new RuntimeException();
    }

    return existingExpenseCategoryLimit;
  }

  static ExpenseCategoryLimitDto expenseCategoryLimitToDto(ExpenseCategoryLimit expenseCategoryLimit) {
    return ExpenseCategoryLimitDto.builder()
        .id(expenseCategoryLimit.getId())
        .expenseCategory(expenseCategoryLimit.getExpenseCategory())
        .expenseLimit(expenseCategoryLimit.getExpenseLimit())
        .build();
  }

  static ExpenseCategoryLimit fromExpenseCategoryLimitDto(ExpenseCategoryLimitDto expenseCategoryLimitDto) {
    return ExpenseCategoryLimit.builder()
        .id(expenseCategoryLimitDto.getId())
        .expenseLimit(expenseCategoryLimitDto.getExpenseLimit())
        .build();
  }
}

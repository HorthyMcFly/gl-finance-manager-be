package com.gl.financemanager.expense;
import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceService;
import com.gl.financemanager.period.PeriodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {

  private final BalanceService balanceService;

  private final ExpenseRepository expenseRepository;
  private final ExpenseCategoryRepository expenseCategoryRepository;
  private final PeriodRepository periodRepository;
  private final UserRepository userRepository;

  public List<ExpenseDto> getExpensesForLoggedInUserByPeriodId(Integer periodId) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return expenseRepository.findByFmUserUsernameAndFmPeriodId(loggedInUsername, periodId)
        .stream().map(ExpenseService::toDto).toList();
  }

  public List<ExpenseCategory> getExpenseCategories() {
    return this.expenseCategoryRepository.findAll();
  }

  @Transactional
  public ExpenseDto createExpense(ExpenseDto expenseDto) {
    if (expenseDto.getId() != null) {
      throw new RuntimeException();
    }
    var newExpense = ExpenseService.fromDto(expenseDto);
    var activePeriod = periodRepository.findByActive(true);
    newExpense.setFmPeriod(activePeriod);
    var loggedInUser = this.userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    if (loggedInUser.isEmpty()) {
      throw new RuntimeException();
    }
    newExpense.setFmUser(loggedInUser.get());

    var createdExpense = expenseRepository.save(newExpense);
    balanceService.updateBalanceForLoggedInUser(expenseDto.getAmount().negate());
    return ExpenseService.toDto(createdExpense);
  }

  @Transactional
  public ExpenseDto modifyExpense(ExpenseDto expenseDto) {
    var existingIncome = this.findExistingExpenseIfValidId(expenseDto.getId());
    if (existingIncome.getLoan() != null) {
      throw new RuntimeException();
    }
    var amountDifference = expenseDto.getAmount().subtract(existingIncome.getAmount());

    existingIncome.setAmount(expenseDto.getAmount());
    existingIncome.setRecipient(expenseDto.getRecipient());
    existingIncome.setExpenseCategory(expenseDto.getExpenseCategory());
    existingIncome.setComment(expenseDto.getComment());

    var modifiedIncome = expenseRepository.save(existingIncome);
    balanceService.updateBalanceForLoggedInUser(amountDifference.negate());

    return ExpenseService.toDto(modifiedIncome);
  }

  @Transactional
  public void deleteExpense(Integer id) {
    var existingIncome = this.findExistingExpenseIfValidId(id);
    if (existingIncome.getLoan() != null) {
      throw new RuntimeException();
    }
    balanceService.updateBalanceForLoggedInUser(existingIncome.getAmount());
    expenseRepository.delete(existingIncome);
  }

  private Expense findExistingExpenseIfValidId(Integer id) {
    if (id == null) {
      throw new RuntimeException();
    }
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var existingExpenseOpt = expenseRepository.findById(id);
    if (existingExpenseOpt.isEmpty()) {
      throw new RuntimeException();
    }
    var existingExpense = existingExpenseOpt.get();
    if (!existingExpense.getFmPeriod().isActive() ||
        !existingExpense.getFmUser().getUsername().equals(loggedInUsername)) {
      throw new RuntimeException();
    }

    return existingExpense;
  }

  static ExpenseDto toDto(Expense expense) {
    return ExpenseDto.builder()
        .id(expense.getId())
        .amount(expense.getAmount())
        .recipient(expense.getRecipient())
        .expenseCategory(expense.getExpenseCategory())
        .comment(expense.getComment())
        .hasRelatedLoan(expense.getLoan() != null)
        .build();
  }

  static Expense fromDto(ExpenseDto expenseDto) {
    return Expense.builder()
        .id(expenseDto.getId())
        .amount(expenseDto.getAmount())
        .recipient(expenseDto.getRecipient())
        .expenseCategory(expenseDto.getExpenseCategory())
        .comment(expenseDto.getComment())
        .build();
  }
}

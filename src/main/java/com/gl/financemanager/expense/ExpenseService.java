package com.gl.financemanager.expense;
import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.balance.BalanceService;
import com.gl.financemanager.loan.Loan;
import com.gl.financemanager.loan.LoanDto;
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
    if (expenseDto.getLoan() != null) {
      newExpense.setLoan(expenseDto.getLoan());
    }

    var createdExpense = expenseRepository.save(newExpense);
    balanceService.updateBalanceForLoggedInUser(expenseDto.getAmount().negate());
    return ExpenseService.toDto(createdExpense);
  }

  @Transactional
  public ExpenseDto modifyExpense(ExpenseDto expenseDto) {
    var existingExpense = this.findExistingExpenseIfValidId(expenseDto.getId());
    if (existingExpense.getLoan() != null) {
      throw new RuntimeException();
    }
    var amountDifference = expenseDto.getAmount().subtract(existingExpense.getAmount());

    existingExpense.setAmount(expenseDto.getAmount());
    existingExpense.setRecipient(expenseDto.getRecipient());
    existingExpense.setExpenseCategory(expenseDto.getExpenseCategory());
    existingExpense.setComment(expenseDto.getComment());

    var modifiedExpense = expenseRepository.save(existingExpense);
    balanceService.updateBalanceForLoggedInUser(amountDifference.negate());

    return ExpenseService.toDto(modifiedExpense);
  }

  @Transactional
  public void deleteExpense(Integer id) {
    var existingExpense = this.findExistingExpenseIfValidId(id);
    if (existingExpense.getLoan() != null) {
      throw new RuntimeException();
    }
    balanceService.updateBalanceForLoggedInUser(existingExpense.getAmount());
    expenseRepository.delete(existingExpense);
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


  @Transactional
  public void createExpenseForLoan(Loan loan) {
    var fixExpenseCategoryOpt = expenseCategoryRepository.findByCategory("Fix");
    assert fixExpenseCategoryOpt.isPresent();
    var newExpense = ExpenseDto.builder()
        .amount(loan.getMonthlyRepayment())
        .loan(loan)
        .recipient("Hitel törlesztő")
        .expenseCategory(fixExpenseCategoryOpt.get())
        .build();
    this.createExpense(newExpense);
  }

  @Transactional
  public void modifyExpenseForLoan(LoanDto loanDto) {
    var activePeriod = periodRepository.findByActive(true);
    var existingExpense =
        expenseRepository.findByLoanIdAndFmPeriodId(loanDto.getId(), activePeriod.getId());
    if (existingExpense == null) {
      throw new RuntimeException();
    }
    var monthlyRepaymentDifference =
        existingExpense.getAmount().subtract(loanDto.getMonthlyRepayment());
    existingExpense.setAmount(loanDto.getMonthlyRepayment());
    expenseRepository.save(existingExpense);
    balanceService.updateBalanceForLoggedInUser(monthlyRepaymentDifference);
  }

  @Transactional
  public void deleteExpenseForLoan(Integer loanId) {
    var activePeriod = periodRepository.findByActive(true);
    var existingExpense =
        expenseRepository.findByLoanIdAndFmPeriodId(loanId, activePeriod.getId());
    balanceService.updateBalanceForLoggedInUser(existingExpense.getAmount());
    expenseRepository.delete(existingExpense);

    var loanExpenses = expenseRepository.findAllByLoanId(loanId);
    loanExpenses.forEach(loanExpense -> {
      loanExpense.setLoan(null);
      loanExpense.setComment("Hitel törölve");
    });
    expenseRepository.saveAll(loanExpenses);
  }

  static ExpenseDto toDto(Expense expense) {
    return ExpenseDto.builder()
        .id(expense.getId())
        .amount(expense.getAmount())
        .recipient(expense.getRecipient())
        .expenseCategory(expense.getExpenseCategory())
        .comment(expense.getComment())
        .relatedLoanName(expense.getLoan() != null ? expense.getLoan().getName() : null)
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

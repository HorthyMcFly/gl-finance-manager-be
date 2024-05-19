package com.gl.financemanager.loan;

import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.expense.ExpenseService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoanService {

  private final ExpenseService expenseService;

  private LoanRepository loanRepository;
  private final UserRepository userRepository;

  public List<LoanDto> getLoansForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return this.loanRepository.findByFmUserUsername(loggedInUsername)
        .stream().map(LoanService::toDto).toList();
  }

  @Transactional
  public LoanDto createLoan(LoanDto loanDto) {
    if (loanDto.getId() != null) {
      throw new RuntimeException();
    }
    var newLoan = LoanService.fromDto(loanDto);
    var loggedInUser = this.userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    if (loggedInUser.isEmpty()) {
      throw new RuntimeException();
    }
    newLoan.setFmUser(loggedInUser.get());

    var createdLoan = loanRepository.save(newLoan);
    expenseService.createExpenseForLoan(createdLoan);
    return LoanService.toDto(createdLoan);
  }

  @Transactional
  public LoanDto modifyLoan(LoanDto loanDto) {
    var existingLoan = this.findExistingLoanIfValidId(loanDto.getId());

    existingLoan.setAmount(loanDto.getAmount());
    existingLoan.setName(loanDto.getName());
    existingLoan.setInterestRate(loanDto.getInterestRate());
    existingLoan.setMonthlyRepayment(loanDto.getMonthlyRepayment());

    var modifiedLoan = loanRepository.save(existingLoan);
    expenseService.modifyExpenseForLoan(loanDto);

    return LoanService.toDto(modifiedLoan);
  }

  @Transactional
  public void deleteLoan(Integer id) {
    var existingLoan = this.findExistingLoanIfValidId(id);
    expenseService.deleteExpenseForLoan(id);
    loanRepository.delete(existingLoan);
  }

  private Loan findExistingLoanIfValidId(Integer id) {
    if (id == null) {
      throw new RuntimeException();
    }
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var existingLoanOpt = loanRepository.findById(id);
    if (existingLoanOpt.isEmpty()) {
      throw new RuntimeException();
    }
    var existingLoan = existingLoanOpt.get();
    if (!existingLoan.getFmUser().getUsername().equals(loggedInUsername)) {
      throw new RuntimeException();
    }

    return existingLoan;
  }

  static LoanDto toDto(Loan loan) {
    return LoanDto.builder()
        .id(loan.getId())
        .amount(loan.getAmount())
        .name(loan.getName())
        .interestRate(loan.getInterestRate())
        .monthlyRepayment(loan.getMonthlyRepayment())
        .build();
  }

  static Loan fromDto(LoanDto loanDto) {
    return Loan.builder()
        .id(loanDto.getId())
        .amount(loanDto.getAmount())
        .name(loanDto.getName())
        .interestRate(loanDto.getInterestRate())
        .monthlyRepayment(loanDto.getMonthlyRepayment())
        .build();
  }
}

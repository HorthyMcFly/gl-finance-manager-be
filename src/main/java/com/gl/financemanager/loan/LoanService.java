package com.gl.financemanager.loan;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoanService {

  private LoanRepository loanRepository;

  public List<LoanDto> getLoansForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return this.loanRepository.findByFmUserUsername(loggedInUsername)
        .stream().map(LoanService::toDto).toList();
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
}

package com.gl.financemanager.balance;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BalanceService {

  private final BalanceRepository balanceRepository;

  public BalanceDto getBalanceForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var balance = this.balanceRepository.findBalanceByFmUserUsername(loggedInUsername);
    return balance.map(BalanceService::toDto).orElse(null);
  }

  static BalanceDto toDto(Balance balance) {
    return BalanceDto
        .builder()
        .id(balance.getId())
        .balance(balance.getBalance())
        .investmentBalance(balance.getInvestmentBalance())
        .build();
  }
}

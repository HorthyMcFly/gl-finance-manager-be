package com.gl.financemanager.balance;

import com.gl.financemanager.auth.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BalanceService {

  private final BalanceRepository balanceRepository;
  private final UserRepository userRepository;

  public BalanceDto getBalanceForLoggedInUser() {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var balance = this.balanceRepository.findBalanceByFmUserUsername(loggedInUsername);
    return balance.map(BalanceService::toDto).orElse(null);
  }

  public void updateBalanceForLoggedInUser(BigDecimal balanceChange) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var balanceOpt = this.balanceRepository.findBalanceByFmUserUsername(loggedInUsername);
    Balance balanceEntity;
    if (balanceOpt.isEmpty()) {
      var loggedInUser = userRepository.findByUsername(loggedInUsername);
      assert loggedInUser.isPresent();
      balanceEntity = Balance.builder()
          .balance(balanceChange)
          .investmentBalance(new BigDecimal(0))
          .fmUser(loggedInUser.get())
          .build();
    } else {
      balanceEntity = balanceOpt.get();
      var newBalanceValue = balanceEntity.getBalance().add(balanceChange);
      balanceEntity.setBalance(newBalanceValue);
    }
    balanceRepository.save(balanceEntity);
  }

  public void updateInvestmentBalanceForLoggedInUser(BigDecimal investmentBalanceChange) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var balanceOpt = this.balanceRepository.findBalanceByFmUserUsername(loggedInUsername);
    Balance balanceEntity;
    if (balanceOpt.isEmpty()) {
      var loggedInUser = userRepository.findByUsername(loggedInUsername);
      assert loggedInUser.isPresent();
      balanceEntity = Balance.builder()
          .balance(new BigDecimal(0))
          .investmentBalance(investmentBalanceChange)
          .fmUser(loggedInUser.get())
          .build();
    } else {
      balanceEntity = balanceOpt.get();
      var newInvestmentBalanceValue =
          balanceEntity.getInvestmentBalance().add(investmentBalanceChange);
      balanceEntity.setInvestmentBalance(newInvestmentBalanceValue);
    }
    balanceRepository.save(balanceEntity);
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

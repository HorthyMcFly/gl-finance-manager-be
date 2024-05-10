package com.gl.financemanager.income;

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
public class IncomeService {

  private final BalanceService balanceService;

  private final IncomeRepository incomeRepository;
  private final PeriodRepository periodRepository;
  private final UserRepository userRepository;

  public List<IncomeDto> getIncomesForLoggedInUserByPeriodId(Integer periodId) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return incomeRepository.findByFmUserUsernameAndFmPeriodId(loggedInUsername, periodId)
        .stream().map(IncomeService::toDto).toList();
  }

  @Transactional
  public IncomeDto createIncome(IncomeDto incomeDto) {
    if (incomeDto.getId() != null) {
      throw new RuntimeException();
    }
    var newIncome = IncomeService.fromDto(incomeDto);
    var activePeriod = periodRepository.findByActive(true);
    newIncome.setFmPeriod(activePeriod);
    var loggedInUser = this.userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    if (loggedInUser.isEmpty()) {
      throw new RuntimeException();
    }
    newIncome.setFmUser(loggedInUser.get());

    var createdIncome = incomeRepository.saveAndFlush(newIncome);
    balanceService.updateBalanceForLoggedInUser(incomeDto.getAmount());
    return IncomeService.toDto(createdIncome);
  }

  @Transactional
  public IncomeDto modifyIncome(IncomeDto incomeDto) {
    var existingIncome = this.findExistingIncomeIfValidId(incomeDto.getId());
    var amountDifference = incomeDto.getAmount().subtract(existingIncome.getAmount());

    existingIncome.setAmount(incomeDto.getAmount());
    existingIncome.setSource(incomeDto.getSource());
    existingIncome.setComment(incomeDto.getComment());

    var modifiedIncome = incomeRepository.saveAndFlush(existingIncome);
    balanceService.updateBalanceForLoggedInUser(amountDifference);

    return IncomeService.toDto(modifiedIncome);
  }

  @Transactional
  public void deleteIncome(Integer id) {
    var existingIncome = this.findExistingIncomeIfValidId(id);
    balanceService.updateBalanceForLoggedInUser(existingIncome.getAmount().negate());
    incomeRepository.delete(existingIncome);
  }

  private Income findExistingIncomeIfValidId(Integer id) {
    if (id == null) {
      throw new RuntimeException();
    }
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    var existingIncomeOpt = incomeRepository.findById(id);
    if (existingIncomeOpt.isEmpty()) {
      throw new RuntimeException();
    }
    var existingIncome = existingIncomeOpt.get();
    if (!existingIncome.getFmPeriod().isActive() ||
        !existingIncome.getFmUser().getUsername().equals(loggedInUsername)) {
      throw new RuntimeException();
    }

    return existingIncome;
  }

  static IncomeDto toDto(Income income) {
    return IncomeDto.builder()
        .id(income.getId())
        .amount(income.getAmount())
        .source(income.getSource())
        .comment(income.getComment())
        .build();
  }

  static Income fromDto(IncomeDto incomeDto) {
    return Income.builder()
        .id(incomeDto.getId())
        .amount(incomeDto.getAmount())
        .source(incomeDto.getSource())
        .comment(incomeDto.getComment())
        .build();
  }

}

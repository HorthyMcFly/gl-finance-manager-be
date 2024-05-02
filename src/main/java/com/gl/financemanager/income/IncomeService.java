package com.gl.financemanager.income;

import com.gl.financemanager.auth.UserRepository;
import com.gl.financemanager.period.PeriodRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IncomeService {

  private final IncomeRepository incomeRepository;
  private final PeriodRepository periodRepository;
  private final UserRepository userRepository;

  public List<IncomeDto> getIncomesForLoggedInUserByPeriodId(Integer periodId) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return incomeRepository.findByFmUserUsernameAndFmPeriodId(loggedInUsername, periodId)
        .stream().map(IncomeService::toDto).toList();
  }

  public IncomeDto createIncome(IncomeDto incomeDto) {
    if (incomeDto.getId() != null) {
      throw new RuntimeException();
    }
    var newIncome = IncomeService.fromDto(incomeDto);
    var activePeriod = periodRepository.findByActive(true);
    newIncome.setFmPeriod(activePeriod);
    var loggedInUser = this.userRepository
        .findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    newIncome.setFmUser(loggedInUser.get());

    var createdIncome = incomeRepository.saveAndFlush(newIncome);
    return IncomeService.toDto(createdIncome);
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

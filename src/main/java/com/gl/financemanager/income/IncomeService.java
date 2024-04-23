package com.gl.financemanager.income;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IncomeService {

  private final IncomeRepository incomeRepository;

  public List<IncomeDto> getIncomesForLoggedInUserByPeriodId(Integer periodId) {
    var loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    return incomeRepository.findByFmUserUsernameAndFmPeriodId(loggedInUsername, periodId)
        .stream().map(IncomeService::toDto).toList();
  }

  static IncomeDto toDto(Income income) {
    return IncomeDto.builder()
        .id(income.getId())
        .amount(income.getAmount())
        .source(income.getSource())
        .comment(income.getComment())
        .build();
  }

}

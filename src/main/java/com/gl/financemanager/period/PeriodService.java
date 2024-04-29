package com.gl.financemanager.period;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PeriodService {

  private final PeriodRepository periodRepository;

  public List<FmPeriod> getPeriods() {
    return periodRepository.findAll();
  }

  public FmPeriod getActivePeriod() {
    return periodRepository.findByActive(true);
  }
}

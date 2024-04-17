package com.gl.financemanager.period;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PeriodService {

  private final PeriodRepository periodRepository;

  List<Period> getPeriods() {
    return this.periodRepository.findAll();
  }
}

package com.gl.financemanager.income;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IncomeService {

  private final IncomeRepository incomeRepository;
}

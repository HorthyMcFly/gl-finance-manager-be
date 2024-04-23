package com.gl.financemanager.income;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/incomes")
@AllArgsConstructor
public class IncomeController {

  private IncomeService incomeService;

  @GetMapping("/own/periods/{periodId}")
  @ResponseBody
  public ResponseEntity<List<IncomeDto>> getIncomesByPeriodId(@PathVariable Integer periodId) {
    return new ResponseEntity<>(this.incomeService.getIncomesForLoggedInUserByPeriodId(periodId),
        HttpStatusCode.valueOf(200));
  }
}

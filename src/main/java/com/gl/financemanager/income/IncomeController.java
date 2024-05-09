package com.gl.financemanager.income;

import jakarta.validation.Valid;
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

  @PostMapping
  @ResponseBody
  public ResponseEntity<IncomeDto> createIncome(@RequestBody @Valid IncomeDto incomeDto) {
    return new ResponseEntity<>(incomeService.createIncome(incomeDto),
        HttpStatusCode.valueOf(201));
  }

  @PutMapping
  @ResponseBody
  public ResponseEntity<IncomeDto> modifyIncome(@RequestBody @Valid IncomeDto incomeDto) {
    return new ResponseEntity<>(incomeService.modifyIncome(incomeDto),
        HttpStatusCode.valueOf(200));
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> deleteIncome(@PathVariable Integer id) {
    this.incomeService.deleteIncome(id);
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }
}

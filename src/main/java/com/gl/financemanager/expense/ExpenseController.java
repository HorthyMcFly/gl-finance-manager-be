package com.gl.financemanager.expense;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/expenses")
@AllArgsConstructor
public class ExpenseController {

  private ExpenseService expenseService;

  @GetMapping("/own/periods/{periodId}")
  @ResponseBody
  public ResponseEntity<List<ExpenseDto>> getIncomesByPeriodId(@PathVariable Integer periodId) {
    return new ResponseEntity<>(this.expenseService.getExpensesForLoggedInUserByPeriodId(periodId),
        HttpStatusCode.valueOf(200));
  }

  @GetMapping("/expense-categories")
  @ResponseBody
  public List<ExpenseCategory> getExpenseCategories() {
    return this.expenseService.getExpenseCategories();
  }

}

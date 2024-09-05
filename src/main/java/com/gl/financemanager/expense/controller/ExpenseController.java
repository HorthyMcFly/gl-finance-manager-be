package com.gl.financemanager.expense.controller;

import com.gl.financemanager.expense.service.ExpenseService;
import com.gl.financemanager.expense.dto.ExpenseDto;
import jakarta.validation.Valid;
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
  public ResponseEntity<List<ExpenseDto>> getExpensesByPeriodId(@PathVariable Integer periodId) {
    return new ResponseEntity<>(
        this.expenseService.getExpensesForLoggedInUserByPeriodId(periodId),
        HttpStatusCode.valueOf(200));
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<ExpenseDto> createExpense(@RequestBody @Valid ExpenseDto expenseDto) {
    return new ResponseEntity<>(expenseService.createExpense(expenseDto),
        HttpStatusCode.valueOf(201));
  }

  @PutMapping
  @ResponseBody
  public ResponseEntity<ExpenseDto> modifyExpense(@RequestBody @Valid ExpenseDto expenseDto) {
    return new ResponseEntity<>(expenseService.modifyExpense(expenseDto),
        HttpStatusCode.valueOf(200));
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> deleteExpense(@PathVariable Integer id) {
    this.expenseService.deleteExpense(id);
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }

}

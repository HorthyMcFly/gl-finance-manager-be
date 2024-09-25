package com.gl.financemanager.expense.controller;

import com.gl.financemanager.expense.dto.ExpenseCategoryLimitDto;
import com.gl.financemanager.expense.entity.ExpenseCategory;
import com.gl.financemanager.expense.service.ExpenseCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/expense-categories")
@AllArgsConstructor
public class ExpenseCategoryController {

  private ExpenseCategoryService expenseCategoryService;

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<ExpenseCategory>> getExpenseCategories() {
    return new ResponseEntity<>(this.expenseCategoryService.getExpenseCategories(),
        HttpStatusCode.valueOf(200));
  }

  @GetMapping("/limits")
  @ResponseBody
  public ResponseEntity<List<ExpenseCategoryLimitDto>> getExpenseCategoryLimits() {
    return new ResponseEntity<>(expenseCategoryService.findAllExpenseCategoryLimitsForLoggedInUser(),
        HttpStatusCode.valueOf(200));
  }

  @PostMapping("/limits")
  @ResponseBody
  public ResponseEntity<ExpenseCategoryLimitDto> createExpenseCategoryLimit(
      @RequestBody @Valid ExpenseCategoryLimitDto expenseCategoryLimitDto) {
    return new ResponseEntity<>(expenseCategoryService.createExpenseCategoryLimit(expenseCategoryLimitDto),
        HttpStatusCode.valueOf(201));
  }

  @PutMapping("/limits")
  @ResponseBody
  public ResponseEntity<ExpenseCategoryLimitDto> modifyExpenseCategoryLimit(
      @RequestBody @Valid ExpenseCategoryLimitDto expenseCategoryLimitDto) {
    return new ResponseEntity<>(expenseCategoryService.modifyExpenseCategoryLimit(expenseCategoryLimitDto),
        HttpStatusCode.valueOf(200));
  }

  @DeleteMapping("/limits/{id}")
  @ResponseBody
  public ResponseEntity<Void> deleteExpenseCategoryLimit(@PathVariable Integer id) {
    expenseCategoryService.deleteExpenseCategoryLimit(id);
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }
}

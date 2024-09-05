package com.gl.financemanager.expense.controller;

import com.gl.financemanager.expense.dto.ExpenseCategoryLimitDto;
import com.gl.financemanager.expense.entity.ExpenseCategory;
import com.gl.financemanager.expense.service.ExpenseCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}

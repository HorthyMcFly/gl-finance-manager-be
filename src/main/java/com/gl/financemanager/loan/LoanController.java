package com.gl.financemanager.loan;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/loans")
@AllArgsConstructor
public class LoanController {

  private LoanService loanService;

  @GetMapping
  @ResponseBody
  public List<LoanDto> getLoansForLoggedInUser() {
    return this.loanService.getLoansForLoggedInUser();
  }
}

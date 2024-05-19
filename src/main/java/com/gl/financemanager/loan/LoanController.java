package com.gl.financemanager.loan;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  @ResponseBody
  public ResponseEntity<LoanDto> createLoan(@RequestBody @Valid LoanDto loanDto) {
    return new ResponseEntity<>(loanService.createLoan(loanDto),
        HttpStatusCode.valueOf(201));
  }

  @PutMapping
  @ResponseBody
  public ResponseEntity<LoanDto> modifyLoan(@RequestBody @Valid LoanDto loanDto) {
    return new ResponseEntity<>(loanService.modifyLoan(loanDto),
        HttpStatusCode.valueOf(200));
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public ResponseEntity<Void> deleteLoan(@PathVariable Integer id) {
    this.loanService.deleteLoan(id);
    return new ResponseEntity<>(null, HttpStatusCode.valueOf(204));
  }
}

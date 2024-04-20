package com.gl.financemanager.balance;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/balance")
@AllArgsConstructor
public class BalanceController {

  private final BalanceService balanceService;

  @GetMapping
  @ResponseBody
  ResponseEntity<BalanceDto> getBalance() {
    return new ResponseEntity<>(this.balanceService.getBalanceForLoggedInUser(),
        HttpStatusCode.valueOf(200));
  }
}

package com.gl.financemanager.period;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/periods")
@AllArgsConstructor
public class PeriodController {

  private final PeriodService periodService;

  @GetMapping
  @ResponseBody
  ResponseEntity<List<FmPeriod>> getPeriods() {
    return new ResponseEntity<>(periodService.getPeriods(),
        HttpStatusCode.valueOf(200));
  }

  @GetMapping("/active")
  @ResponseBody
  ResponseEntity<FmPeriod> getActivePeriod() {
    return new ResponseEntity<>(periodService.getActivePeriod(), HttpStatusCode.valueOf(200));
  }
}
